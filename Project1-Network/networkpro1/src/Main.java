public class Main {

    public static void main(String[] args) {
        // Run the server class
        TCPServer server = new TCPServer();
        new Thread(() -> {
            server.main(new String[]{});
        }).start();

        // Run the client class
        TCPClient client = new TCPClient();
        new Thread(() -> {
            client.startClient();
        }).start();
    }
}
