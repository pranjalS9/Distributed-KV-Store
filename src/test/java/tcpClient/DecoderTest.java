package tcpClient;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class DecoderTest {

    @Test
    void shouldDecodeEncodedMessageToOriginalRawMessage() {
        RawMessage rawMessage = new RawMessage(1L, "name".getBytes(), "Pranjal".getBytes());
        byte[] encodedMessage = Encoder.encode(rawMessage);
        RawMessage decodedMessage = Decoder.decode(encodedMessage);

        assertEquals(rawMessage.getReqId(), decodedMessage.getReqId());
        assertArrayEquals(rawMessage.getKey(), decodedMessage.getKey());
        assertArrayEquals(rawMessage.getValue(), decodedMessage.getValue());
    }
}
