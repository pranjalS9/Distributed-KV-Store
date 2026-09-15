package tcpClient;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EncoderTest {

    @Test
    void shouldReturnSameOutputLengthAsInput() {
        RawMessage rawMessage = new RawMessage(1L, "name".getBytes(), "Pranjal".getBytes());
        byte[] encodedMessage = Encoder.encode(rawMessage);

        assertEquals(27, encodedMessage.length);
    }
}
