package tcpClient;

import org.junit.jupiter.api.Test;

import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class RequestWaitlistTest {

    @Test
    void shouldReturnCompletableFutureOnRequestRegistration() {
        RequestWaitlist waitList = new RequestWaitlist();
        CompletableFuture<RawMessage> future = waitList.register(1L);

        assertNotNull(future);
    }
}
