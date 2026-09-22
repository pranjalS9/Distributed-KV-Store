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

    @Test
    void shouldHandleMultipleSequentialPutsOnSameConnection() throws IOException, ExecutionException, InterruptedException {
        MockAPIServer server = new MockAPIServer(0);
        server.start();

        Client client = new Client("localhost", server.getPort());
        CompletableFuture<RawMessage> future1 = client.put("name".getBytes(), "Pranjal".getBytes());
        CompletableFuture<RawMessage> future2 = client.put("age".getBytes(), "21".getBytes());

        RawMessage response1 = future1.get();
        RawMessage response2 = future2.get();

        assertNotNull(response1);
        assertNotNull(response2);

        assertArrayEquals("ok".getBytes(), response1.getKey());
        assertArrayEquals("ok".getBytes(), response2.getKey());

        server.stop();
    }
}
