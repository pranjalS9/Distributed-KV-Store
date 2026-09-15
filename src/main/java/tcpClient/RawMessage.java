package tcpClient;

public class RawMessage {
    private final long reqId;
    private final byte[] key;
    private final byte[] value;

    public RawMessage(long reqId, byte[] key, byte[] value) {
        this.reqId = reqId;
        this.key = key;
        this.value = value;
    }

    public long getReqId() { return reqId; }
    public byte[] getKey() { return key; }
    public byte[] getValue() { return value; }
}
