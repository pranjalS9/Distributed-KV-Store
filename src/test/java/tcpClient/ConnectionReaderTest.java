package tcpClient;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ConnectionReaderTest {

    @Test
    void shouldReadResponseAndCompleteTheFuture() throws IOException, ExecutionException, InterruptedException {
        MockAPIServer server = new MockAPIServer(0);
        server.start();

        Socket socket = new Socket("localhost", server.getPort());

        RequestWaitlist waitList = new RequestWaitlist();
        CompletableFuture<RawMessage> future = waitList.register(1L);

        MessageSender.send(socket.getOutputStream(), Encoder.encode(new RawMessage(1L, "name".getBytes(), "Pranjal".getBytes())));

        ConnectionReader reader = new ConnectionReader(socket.getInputStream(), waitList);
        reader.start();

        assertEquals(1L, future.get().getReqId());

        socket.close();
    }
}
