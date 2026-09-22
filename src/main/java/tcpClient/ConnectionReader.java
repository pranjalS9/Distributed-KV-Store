package tcpClient;

import java.io.IOException;
import java.io.InputStream;

public class ConnectionReader {
    private final RequestWaitlist waitlist;
    private final InputStream inputStream;

    ConnectionReader(InputStream inputStream, RequestWaitlist requestWaitlist) {
        this.inputStream = inputStream;
        this.waitlist = requestWaitlist;
    }

    public void start() {
        Thread thread = new Thread(() -> {
            try {
                while (true) {
                    RawMessage response = Decoder.decode(MessageReader.read(inputStream));
                    waitlist.complete(response.getReqId(), response);
                }
            } catch (IOException ignored) { }
        });
        thread.setDaemon(true);
        thread.start();
    }
}
