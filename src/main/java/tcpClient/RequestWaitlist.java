package tcpClient;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public class RequestWaitlist {
    private final ConcurrentHashMap<Long, CompletableFuture<RawMessage>> waitlist = new ConcurrentHashMap<>();

    public CompletableFuture<RawMessage> register(long reqId) {
        CompletableFuture<RawMessage> future = new CompletableFuture<>();
        waitlist.put(reqId, future);
        return future;
    }

    public void complete(long reqId, RawMessage rawMessage) {
        CompletableFuture<RawMessage> future = waitlist.get(reqId);

        if(future != null) {
            waitlist.remove(reqId);
            future.complete(rawMessage);
        }
    }
}
