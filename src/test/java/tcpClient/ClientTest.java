package tcpClient;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;

public class ClientTest {
    @Test
    void shouldReturnCompletedFutureAfterPut() throws IOException, ExecutionException, InterruptedException {
        MockAPIServer server = new MockAPIServer(0);
        server.start();

        Client client = new Client("localhost", server.getPort());
        CompletableFuture<RawMessage> future = client.put("name".getBytes(), "Pranjal".getBytes());
        RawMessage response = future.get();

        assertNotNull(response);
        assertArrayEquals("ok".getBytes(), response.getKey());

        server.stop();
    }
}
