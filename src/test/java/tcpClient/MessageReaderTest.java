package tcpClient;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

public class MessageReaderTest {

    @Test
    void shouldReadExactMessageBytesAfterLengthPrefix() throws IOException {
        RawMessage rawMessage = new RawMessage(1L, "ok".getBytes(), null);
        byte[] encodedMessage = Encoder.encode(rawMessage);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        MessageSender.send(outputStream, encodedMessage);
        byte[] writtenMessage = outputStream.toByteArray();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(writtenMessage);
        byte[] result = MessageReader.read(inputStream);

        assertArrayEquals(encodedMessage, result);
    }

    @Test
    void shouldProperlyReadMultipleMessages() throws IOException {
        RawMessage rawMessage1 = new RawMessage(1L, "name".getBytes(), "Pranjal".getBytes());
        RawMessage rawMessage2 = new RawMessage(1L, "ok".getBytes(), null);

        byte[] encodedMessage1 = Encoder.encode(rawMessage1);
        byte[] encodedMessage2 = Encoder.encode(rawMessage2);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        MessageSender.send(outputStream, encodedMessage1);
        MessageSender.send(outputStream, encodedMessage2);

        byte[] writtenMessages = outputStream.toByteArray();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(writtenMessages);
        byte[] result1 = MessageReader.read(inputStream);
        byte[] result2 = MessageReader.read(inputStream);

        assertArrayEquals(encodedMessage1, result1);
        assertArrayEquals(encodedMessage2, result2);
    }
}
