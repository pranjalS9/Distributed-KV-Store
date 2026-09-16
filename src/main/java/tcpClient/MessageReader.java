package tcpClient;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

public class MessageReader {
    public static byte[] read(InputStream inputStream) throws IOException {
        DataInputStream stream = new DataInputStream(inputStream);
        int messageLength = stream.readInt(); // reads exactly 4 bytes, combines them correctly

        byte[] result = new byte[messageLength];
        stream.readFully(result); // "readFully": keeps reading until it has exactly the right number of bytes

        return result;
    }
}
