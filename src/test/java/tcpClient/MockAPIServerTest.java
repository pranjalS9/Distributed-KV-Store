package tcpClient;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.Socket;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MockAPIServerTest {
    @Test
    void shouldRespondWithSameReqIdAsRequest() throws IOException {
        MockAPIServer server = new MockAPIServer(0);
        server.start();

        Socket socket = new Socket("localhost", server.getPort());

        RawMessage rawMessage = new RawMessage(1L, "name".getBytes(), "Pranjal".getBytes());
        byte[] encodedMessage = Encoder.encode(rawMessage);
        MessageSender.send(socket.getOutputStream(), encodedMessage);

        byte[] encodedResponse = MessageReader.read(socket.getInputStream());
        RawMessage response = Decoder.decode(encodedResponse);

        assertEquals(1L, response.getReqId());

        socket.close();
        server.stop();
    }

    @Test
    void shouldHandleMultipleSequentialRequestsOnSameConnection() throws IOException {
        MockAPIServer server = new MockAPIServer(0);
        server.start();

        Socket socket = new Socket("localhost", server.getPort());

        RawMessage rawMessage1 = new RawMessage(1L, "name".getBytes(), "Pranjal".getBytes());
        RawMessage rawMessage2 = new RawMessage(2L, "age".getBytes(), "23".getBytes());

        byte[] encodedMessage1 = Encoder.encode(rawMessage1);
        byte[] encodedMessage2 = Encoder.encode(rawMessage2);

        MessageSender.send(socket.getOutputStream(), encodedMessage1);
        MessageSender.send(socket.getOutputStream(), encodedMessage2);

        byte[] encodedResponse1 = MessageReader.read(socket.getInputStream());
        byte[] encodedResponse2 = MessageReader.read(socket.getInputStream());

        RawMessage response1 = Decoder.decode(encodedResponse1);
        RawMessage response2 = Decoder.decode(encodedResponse2);

        assertEquals(1L, response1.getReqId());
        assertEquals(2L, response2.getReqId());

        socket.close();
        server.stop();
    }
}
