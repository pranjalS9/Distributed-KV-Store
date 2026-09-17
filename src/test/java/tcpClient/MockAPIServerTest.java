package tcpClient;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.Socket;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MockAPIServerTest {
    @Test
    void shouldRespondWithSameReqIdAsRequest() throws IOException {
        MockAPIServer server = new MockAPIServer(9090);
        server.start();

        Socket socket = new Socket("localhost", 9090);

        RawMessage rawMessage = new RawMessage(1L, "name".getBytes(), "Pranjal".getBytes());
        byte[] encodedMessage = Encoder.encode(rawMessage);
        MessageSender.send(socket.getOutputStream(), encodedMessage);

        byte[] encodedResponse = MessageReader.read(socket.getInputStream());
        RawMessage response = Decoder.decode(encodedResponse);

        assertEquals(1L, response.getReqId());

        socket.close();
        server.stop();
    }
}
