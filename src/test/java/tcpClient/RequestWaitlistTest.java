package tcpClient;

import org.junit.jupiter.api.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class RequestWaitlistTest {

    @Test
    void shouldReturnCompletableFutureOnRequestRegistration() {
        RequestWaitlist waitList = new RequestWaitlist();
        CompletableFuture<RawMessage> future = waitList.register(1L);

        assertNotNull(future);
    }

    @Test
    void shouldCompleteFutureWhenResponseComesBack() throws ExecutionException, InterruptedException {
        RawMessage rawMessage = new RawMessage(1L, "name".getBytes(), "Pranjal".getBytes());

        RequestWaitlist waitList = new RequestWaitlist();
        CompletableFuture<RawMessage> future = waitList.register(1L);

        waitList.complete(1L, rawMessage);

        assertEquals(future.get(), rawMessage);
    }
}
