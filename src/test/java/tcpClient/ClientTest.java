package tcpClient;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

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

    @Test
    void shouldHandleConcurrentPutsOnSameConnection() throws ExecutionException, InterruptedException, IOException {
        MockAPIServer server = new MockAPIServer(0);
        server.start();

        Client client = new Client("localhost", server.getPort());
        int count = 10;

        List<CompletableFuture<RawMessage>> futures = new ArrayList<>();
        ExecutorService executorService = Executors.newFixedThreadPool(count);
        CountDownLatch latch = new CountDownLatch(count);

        for(int i = 0; i < count; i++) {
            executorService.submit(() -> {
                try {
                    futures.add(client.put("name".getBytes(), "Pranjal".getBytes()));
                    latch.countDown();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }
        latch.await();

        for(int i = 0; i < count; i++) {
            RawMessage response = futures.get(i).get();
            assertNotNull(response);
            assertArrayEquals("ok".getBytes(), response.getKey());
        }
        executorService.shutdown();
    }

    @Test
    void shouldFailWithTimeoutExceptionWhenServerDoesNotRespond() throws IOException {
        ServerSocket silentServer = new ServerSocket(0);
        new Thread(() -> {
            try { silentServer.accept(); } catch (IOException ignored) {}
        }).start();

        Client client = new Client("localhost", silentServer.getLocalPort(), 100); // 100ms timeout
        CompletableFuture<RawMessage> future = client.put("name".getBytes(), "Pranjal".getBytes());

        assertThrows(ExecutionException.class, future::get); // future completes exceptionally

        silentServer.close();
    }
}
