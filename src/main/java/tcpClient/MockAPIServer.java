package tcpClient;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class MockAPIServer {
    private final int port;
    private ServerSocket serverSocket;
    private final Map<String, byte[]> store = new HashMap<>();

    MockAPIServer(int port) {
        this.port = port;
    }

    public void start() throws IOException {
        serverSocket = new ServerSocket(port);
        Thread thread = new Thread(() -> {
            try {
                while(!serverSocket.isClosed()) {
                    Socket socket = serverSocket.accept();
                    new Thread(() -> handleConnection(socket)).start();
                }
            } catch (IOException ignored) {}
        });
        thread.setDaemon(true);
        thread.start();
    }

    public void stop() throws IOException {
        serverSocket.close();
    }

    private void handleConnection(Socket socket) {
        try (socket) {
            while (true) {
                byte[] encodedMessage = MessageReader.read(socket.getInputStream());
                RawMessage rawMessage = Decoder.decode(encodedMessage);

                store.put(Arrays.toString(rawMessage.getKey()), rawMessage.getValue());

                RawMessage response = new RawMessage(rawMessage.getReqId(), "ok".getBytes(), null);
                byte[] encodedResponse = Encoder.encode(response);
                MessageSender.send(socket.getOutputStream(), encodedResponse);
            }
        } catch (IOException ignored) { }
    }

    public int getPort() {
        return serverSocket.getLocalPort();
    }
}
