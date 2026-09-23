package tcpClient;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

public class Client {
    private final Socket socket;
    private final RequestWaitlist waitlist;
    private final AtomicLong reqIdCounter;
    private final int timeoutMs;
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    Client(String host, int port) throws IOException {
        this(host, port, 5000);
    }

    Client(String host, int port, int timeout) throws IOException {
        this.socket = new Socket(host, port);
        this.waitlist = new RequestWaitlist();
        this.reqIdCounter = new AtomicLong(0);
        this.timeoutMs = timeout;
        ConnectionReader connectionReader = new ConnectionReader(socket.getInputStream(), waitlist);
        connectionReader.start();
    }

    public CompletableFuture<RawMessage> put(byte[] key, byte[] value) throws IOException {
        long reqId = reqIdCounter.incrementAndGet();
        CompletableFuture<RawMessage> future = waitlist.register(reqId);
        scheduler.schedule(() -> future.completeExceptionally(new TimeoutException()), timeoutMs, TimeUnit.MILLISECONDS);
        MessageSender.send(socket.getOutputStream(), Encoder.encode(new RawMessage(reqId, key, value)));
        return future;
    }
}
