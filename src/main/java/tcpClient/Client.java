package tcpClient;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicLong;

public class Client {
    private final Socket socket;
    private final RequestWaitlist waitlist;
    private final AtomicLong reqIdCounter;

    Client(String host, int port) throws IOException {
        this.socket = new Socket(host, port);
        this.waitlist = new RequestWaitlist();
        this.reqIdCounter = new AtomicLong(0);
        ConnectionReader connectionReader = new ConnectionReader(socket.getInputStream(), waitlist);
        connectionReader.start();
    }

    public CompletableFuture<RawMessage> put(byte[] key, byte[] value) throws IOException {
        long reqId = reqIdCounter.incrementAndGet();
        CompletableFuture<RawMessage> future = waitlist.register(reqId);
        MessageSender.send(socket.getOutputStream(), Encoder.encode(new RawMessage(reqId, key, value)));
        return future;
    }
}
