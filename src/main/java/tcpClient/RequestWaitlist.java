package tcpClient;

import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

public class RequestWaitlist {
    private final HashMap<Long, CompletableFuture<RawMessage>> waitlist = new HashMap<>();

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
