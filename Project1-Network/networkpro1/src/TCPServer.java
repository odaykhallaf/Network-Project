import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServer {

    public static void main(String[] args) {
        final int port = 9955; // Define the port number on which the server will listen

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server is listening on port " + port);

            // Continuously listen for incoming client connections
            while (true) {
                Socket clientSocket = serverSocket.accept(); // Accept incoming client connections
                // Inform that a client has connected
                System.out.println("Client connected.");
                // Handle each client connection in a new thread
                new Thread(() -> handleConnection(clientSocket)).start();
            }
        } catch (IOException e) {
            // Handle any exceptions that may occur during server setup
            System.out.println("Server exception: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Define a method to handle the client connection
    private static void handleConnection(Socket clientSocket) {
        try (InputStream inputStream = clientSocket.getInputStream();
             OutputStream outputStream = clientSocket.getOutputStream()) {

            // Inform that the server is handling the connection to the client
            byte[] buffer = new byte[1024];
            int bytesRead = inputStream.read(buffer);

            String message = new String(buffer, 0, bytesRead).trim();


            // Check if the received message is a valid student ID
            if (isValidStudentID(message)) {
                // Inform that a valid student ID has been received
                System.out.println("Received valid student ID: " + message);
                // Send a response message to the client
                outputStream.write("Server will lock the screen after 10 seconds\n".getBytes());

                try {
                    Thread.sleep(10000);

                    // Call the lockScreen function to simulate locking the screen
                    screenlocker.lockScreen();
                    // Inform that the screen is locked (simulated)
                    System.out.println("Locking screen now (simulate OS lock)");
                } catch (InterruptedException e) {
                    // Handle any exceptions that may occur during the sleep
                    throw new RuntimeException(e);
                }
            } else {
                // Inform that an invalid student ID or message has been received
                System.out.println("Invalid student ID or message: " + message);
                // Send an error message to the client
                outputStream.write("Error: Invalid student ID or message\n".getBytes());
            }

        } catch (IOException e) {
            // Handle any exceptions that may occur during input/output operations
            System.out.println("An exception occurred while handling connection: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Define a method to check if a student ID is valid
    private static boolean isValidStudentID(String studentID) {
        // Define an array of valid student IDs
        String[] validStudentIDs = {"1190546","1200620","1200609"};
        // Check if the received student ID matches any of the valid IDs
        for (String validID : validStudentIDs) {
            if (studentID.equals(validID)) {
                return true; // Return true if the ID is valid
            }
        }
        return false; // Return false if the ID is invalid
    }
}
