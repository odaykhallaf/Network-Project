import java.io.IOException;

public class screenlocker {

    public static void lockScreen() {
        // Retrieve the name of the operating system
        String os = System.getProperty("os.name").toLowerCase();

        // Check if the operating system is Windows
        if (os.contains("win")) {
            // If it's Windows, lock the Windows screen
            lockWindowsScreen();
        } else {
            // If the OS is not Windows, print a message indicating that screen lock is not implemented
            System.out.println("Screen lock not implemented for this OS");
        }
    }

    private static void lockWindowsScreen() {
        try {
            // Execute the command to lock the Windows screen using rundll32.exe
            Process process = Runtime.getRuntime().exec("rundll32.exe user32.dll,LockWorkStation");
            // Wait for the command to complete and get the exit code
            int exitCode = process.waitFor();

            // Check if the command was successful (exit code 0)
            if (exitCode == 0) {
                System.out.println("Windows screen locked");
            } else {
                // If the command failed, print an error message
                System.out.println("Failed to lock Windows screen");
            }
        } catch (IOException | InterruptedException e) {
            // Catch and print any IO or Interruption exceptions
            e.printStackTrace();
        }
    }
}
