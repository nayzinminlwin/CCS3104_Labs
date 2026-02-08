package Chap5.deadlock;

import java.io.*;
import java.net.*;
import java.util.Scanner;

/**
 * Deadlock Demonstration Client
 * Connect multiple instances of this client to the server and execute
 * different locking orders simultaneously to trigger a deadlock.
 * 
 * To demonstrate deadlock:
 * 1. Start the Server
 * 2. Run Client instance 1 and type ORDER1
 * 3. Quickly run Client instance 2 and type ORDER2
 * 4. Both clients will deadlock waiting for each other's resources
 */
public class Client {
    private static final String HOST = "localhost";
    private static final int PORT = 8888;

    public static void main(String[] args) {
        System.out.println("=== Deadlock Demonstration Client ===");
        
        try (Socket socket = new Socket(HOST, PORT);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             Scanner scanner = new Scanner(System.in)) {

            System.out.println("Connected to server at " + HOST + ":" + PORT + "\n");

            // Start a thread to read server responses
            Thread readerThread = new Thread(() -> {
                try {
                    String serverMessage;
                    while ((serverMessage = in.readLine()) != null) {
                        System.out.println("Server: " + serverMessage);
                    }
                } catch (IOException e) {
                    if (!socket.isClosed()) {
                        System.err.println("Error reading from server: " + e.getMessage());
                    }
                }
            });
            readerThread.setDaemon(true);
            readerThread.start();

            // Give the reader thread time to display welcome message
            Thread.sleep(100);

            printInstructions();

            // Read user commands and send to server
            String userInput;
            while (true) {
                System.out.print("\nEnter command: ");
                userInput = scanner.nextLine().trim();
                
                if (userInput.isEmpty()) {
                    continue;
                }
                
                out.println(userInput);
                
                if (userInput.equalsIgnoreCase("QUIT")) {
                    break;
                }
                
                // Give some time for server response
                Thread.sleep(100);
            }

            System.out.println("\nDisconnected from server.");

        } catch (UnknownHostException e) {
            System.err.println("Unknown host: " + HOST);
        } catch (IOException e) {
            System.err.println("I/O error: " + e.getMessage());
        } catch (InterruptedException e) {
            System.err.println("Interrupted: " + e.getMessage());
            Thread.currentThread().interrupt();
        }
    }

    private static void printInstructions() {
        System.out.println("\n╔════════════════════════════════════════════════════════╗");
        System.out.println("║         DEADLOCK DEMONSTRATION (AUTO MODE)             ║");
        System.out.println("╠════════════════════════════════════════════════════════╣");
        System.out.println("║ The server will AUTOMATICALLY execute deadlock:       ║");
        System.out.println("║   - Client 1 (odd): ORDER1 (A->B)                      ║");
        System.out.println("║   - Client 2 (even): ORDER2 (B->A)                     ║");
        System.out.println("║                                                        ║");
        System.out.println("║ To see DEADLOCK:                                       ║");
        System.out.println("║   1. Run TWO client instances (within 1 second)        ║");
        System.out.println("║   2. Watch both clients HANG (DEADLOCK!)               ║");
        System.out.println("║                                                        ║");
        System.out.println("║ Manual commands (optional):                            ║");
        System.out.println("║   ORDER1  - Acquire Resource A first, then B           ║");
        System.out.println("║   ORDER2  - Acquire Resource B first, then A           ║");
        System.out.println("║   QUIT    - Disconnect from server                     ║");
        System.out.println("║                                                        ║");
        System.out.println("║ Why deadlock occurs:                                   ║");
        System.out.println("║   - Client 1 holds Resource A, waits for Resource B    ║");
        System.out.println("║   - Client 2 holds Resource B, waits for Resource A    ║");
        System.out.println("║   - Circular wait → DEADLOCK                           ║");
        System.out.println("╚════════════════════════════════════════════════════════╝");
    }
}
