package tcpClient;

import java.nio.ByteBuffer;

public class Encoder {
    public static byte[] encode(RawMessage rawMessage) {
        long reqId = rawMessage.getReqId();
        byte[] key = rawMessage.getKey();
        byte[] value = rawMessage.getValue();

        int encodedMessageLength = 8 + 4 + key.length + 4 + value.length;
        ByteBuffer buffer = ByteBuffer.allocate(encodedMessageLength); //ByteBuffer defaults to big-endian
        buffer.putLong(reqId);
        buffer.putInt(key.length);
        buffer.put(key);
        buffer.putInt(value.length);
        buffer.put(value);

        return buffer.array();
    }
}
