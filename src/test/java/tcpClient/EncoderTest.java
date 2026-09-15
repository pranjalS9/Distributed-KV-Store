package tcpClient;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class EncoderTest {

    @Test
    void shouldReturnSameOutputLengthAsInput() {
        RawMessage rawMessage = new RawMessage(1L, "name".getBytes(), "Pranjal".getBytes());
        byte[] encodedMessage = Encoder.encode(rawMessage);

        assertEquals(27, encodedMessage.length);
    }

    @Test
    void shouldEncodeReqIdAsFirstEightBytesInBigEndian() {
        RawMessage rawMessage = new RawMessage(1L, "name".getBytes(), "Pranjal".getBytes());
        byte[] encodedMessage = Encoder.encode(rawMessage);

        assertArrayEquals(new byte[]{0, 0, 0, 0, 0, 0, 0, 1}, Arrays.copyOfRange(encodedMessage, 0, 8));
    }

    @Test
    void shouldEncodeKeyCorrectly() {
        RawMessage rawMessage = new RawMessage(1L, "name".getBytes(), "Pranjal".getBytes());
        byte[] encodedMessage = Encoder.encode(rawMessage);

        assertArrayEquals(new byte[]{0, 0, 0, 4}, Arrays.copyOfRange(encodedMessage, 8, 12));
        assertArrayEquals("name".getBytes(), Arrays.copyOfRange(encodedMessage, 12, 16));
    }
}
