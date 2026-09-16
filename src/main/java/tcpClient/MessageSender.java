package tcpClient;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class MessageSender {
    public static void send(OutputStream outputStream, byte[] encodedMessage) throws IOException {
        DataOutputStream stream = new DataOutputStream(outputStream); // writes in big-endian
        stream.writeInt(encodedMessage.length);
        stream.write(encodedMessage);
        stream.flush();
    }
}