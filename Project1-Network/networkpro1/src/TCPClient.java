// Import necessary libraries for input/output and networking
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

// Create a class named TCPClient to implement the client functionality
public class TCPClient {

    // Define a method to start the client
    public static void startClient() {
        final String serverAddress = "localhost"; // Define the server address
        final int serverPort = 9955; // Define the server port

        try (
                Socket socket = new Socket(serverAddress, serverPort); // Create a socket connection to the server
                OutputStream outputStream = socket.getOutputStream(); // Get the output stream for sending data
                InputStream inputStream = socket.getInputStream()) { // Get the input stream for receiving data

            String studentID = "1190546"; // Define a sample student ID
            outputStream.write(studentID.getBytes()); // Send the student ID to the server
            // Display a message indicating the student ID sent to the server
            System.out.println("Sent student ID to the server: " + studentID);

            byte[] buffer = new byte[1024]; // Create a buffer to store received data
            int bytesRead = inputStream.read(buffer); // Read data from the server into the buffer
            // Display the server's response
            System.out.println("Server response: " + new String(buffer, 0, bytesRead));

        } catch (IOException e) {
            // Handle any exceptions that may occur during input/output operations
            e.printStackTrace();
        }
    }

    // Define the main method to start the client
    public static void main(String[] args) {
        startClient(); // Call the startClient method when the client is launched
    }
}
