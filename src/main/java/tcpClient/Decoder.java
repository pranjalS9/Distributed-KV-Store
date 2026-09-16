package tcpClient;

import java.nio.ByteBuffer;

public class Decoder {
    public static RawMessage decode(byte[] encodedMessage) {
        ByteBuffer buffer = ByteBuffer.wrap(encodedMessage);
        long reqId = buffer.getLong();

        int keyLen = buffer.getInt();
        byte[] key = new byte[keyLen];
        buffer.get(key);

        int valueLen = buffer.getInt();
        byte[] value = new byte[valueLen];
        buffer.get(value);

        return new RawMessage(reqId, key, value);
    }
}
