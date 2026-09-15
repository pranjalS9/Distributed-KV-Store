package tcpClient;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RawMessageTest {

    @Test
    void shouldStoreReqIdKeyAndValue() {
        RawMessage rawMessage = new RawMessage(1L, "name".getBytes(), "Pranjal".getBytes());

        assertEquals(1L, rawMessage.getReqId());
        assertArrayEquals("name".getBytes(), rawMessage.getKey());
        assertArrayEquals("Pranjal".getBytes(), rawMessage.getValue());
    }

    @Test
    void shouldAllowNullAsAValue() {
        RawMessage rawMessage = new RawMessage(1L, "name".getBytes(), null);

        assertNull(rawMessage.getValue());
    }
}
