package tcpClient;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

public class MessageReaderTest {

    @Test
    void shouldReadExactMessageBytesAfterLengthPrefix() throws IOException {
        RawMessage rawMessage = new RawMessage(1L, "name".getBytes(), "Pranjal".getBytes());
        byte[] encodedMessage = Encoder.encode(rawMessage);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        MessageSender.send(outputStream, encodedMessage);
        byte[] writtenMessage = outputStream.toByteArray();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(writtenMessage);
        byte[] result = MessageReader.read(inputStream);

        assertArrayEquals(encodedMessage, result);
    }
}
