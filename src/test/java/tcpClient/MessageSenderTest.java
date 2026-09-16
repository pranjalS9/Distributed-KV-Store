package tcpClient;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

public class MessageSenderTest {
    @Test
    void shouldSendMessageWithProperLengthFraming() throws IOException {
        RawMessage rawMessage = new RawMessage(1L, "name".getBytes(), "Pranjal".getBytes());
        byte[] encodedMessage = Encoder.encode(rawMessage);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        MessageSender.send(outputStream, encodedMessage);
        byte[] writtenMessage = outputStream.toByteArray();

        assertArrayEquals(new byte[]{0, 0, 0, 27}, Arrays.copyOfRange(writtenMessage, 0, 4));
        assertArrayEquals(encodedMessage, Arrays.copyOfRange(writtenMessage, 4, writtenMessage.length));
    }
}