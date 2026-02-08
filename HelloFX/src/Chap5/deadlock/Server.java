package Chap5.deadlock;

import java.io.*;
import java.net.*;

/**
 * Deadlock Demonstration Server
 * This server manages two resources and demonstrates deadlock when multiple clients
 * request resources in different orders, creating a circular wait condition.
 */
public class Server {
    private static final int PORT = 8888;
    
    // Two shared resources that will cause deadlock
    private static final Object resourceA = new Object();
    private static final Object resourceB = new Object();
    
    private static int clientCounter = 0;

    public static void main(String[] args) {
        System.out.println("=== Deadlock Demonstration Server ===");
        System.out.println("Server started on port " + PORT);
        System.out.println("Waiting for clients to connect...\n");
        
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                clientCounter++;
                System.out.println("Client " + clientCounter + " connected from " + 
                                   clientSocket.getInetAddress().getHostAddress());
                
                // Create a new thread for each client
                Thread clientHandler = new Thread(new ClientHandler(clientSocket, clientCounter));
                clientHandler.start();
            }
        } catch (IOException e) {
            System.err.println("Server error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Handles each client connection in a separate thread
     */
    static class ClientHandler implements Runnable {
        private Socket socket;
        private int clientId;
        private BufferedReader in;
        private PrintWriter out;

        public ClientHandler(Socket socket, int clientId) {
            this.socket = socket;
            this.clientId = clientId;
        }

        @Override
        public void run() {
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);

                out.println("Connected to server. You are Client " + clientId);
                
                // Automatically execute different orders for different clients
                // Odd clients: ORDER1 (A->B), Even clients: ORDER2 (B->A)
                if (clientId % 2 == 1) {
                    out.println("Auto-executing ORDER1 (A->B) to trigger deadlock...");
                    Thread.sleep(500); // Small delay to ensure both clients connect
                    acquireResourcesOrder1();
                } else {
                    out.println("Auto-executing ORDER2 (B->A) to trigger deadlock...");
                    Thread.sleep(500); // Small delay to ensure both clients connect
                    acquireResourcesOrder2();
                }
                
                // Keep connection alive
                String command;
                while ((command = in.readLine()) != null) {
                    command = command.trim().toUpperCase();
                    
                    if (command.equals("QUIT")) {
                        out.println("Goodbye!");
                        break;
                    } else if (command.equals("ORDER1")) {
                        acquireResourcesOrder1();
                    } else if (command.equals("ORDER2")) {
                        acquireResourcesOrder2();
                    } else {
                        out.println("Unknown command: " + command);
                    }
                }
            } catch (IOException e) {
                System.err.println("Client " + clientId + " error: " + e.getMessage());
            } catch (InterruptedException e) {
                System.err.println("Client " + clientId + " interrupted: " + e.getMessage());
                Thread.currentThread().interrupt();
            } finally {
                cleanup();
            }
        }

        /**
         * Acquires resources in order: A -> B
         * This creates potential for deadlock with acquireResourcesOrder2()
         */
        private void acquireResourcesOrder1() {
            System.out.println("[Client " + clientId + "] Attempting ORDER1: A -> B");
            out.println("Attempting to acquire Resource A...");
            
            synchronized (resourceA) {
                System.out.println("[Client " + clientId + "] ✓ Acquired Resource A");
                out.println("Acquired Resource A");
                
                // Simulate some work with Resource A
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                
                out.println("Attempting to acquire Resource B...");
                System.out.println("[Client " + clientId + "] Waiting for Resource B...");
                
                synchronized (resourceB) {
                    System.out.println("[Client " + clientId + "] ✓ Acquired Resource B");
                    out.println("Acquired Resource B");
                    out.println("SUCCESS: Both resources acquired!");
                    
                    // Simulate work with both resources
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                    
                    System.out.println("[Client " + clientId + "] Released Resource B");
                }
                System.out.println("[Client " + clientId + "] Released Resource A");
            }
            out.println("Resources released. Ready for next command.");
        }

        /**
         * Acquires resources in order: B -> A
         * This creates potential for deadlock with acquireResourcesOrder1()
         */
        private void acquireResourcesOrder2() {
            System.out.println("[Client " + clientId + "] Attempting ORDER2: B -> A");
            out.println("Attempting to acquire Resource B...");
            
            synchronized (resourceB) {
                System.out.println("[Client " + clientId + "] ✓ Acquired Resource B");
                out.println("Acquired Resource B");
                
                // Simulate some work with Resource B
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                
                out.println("Attempting to acquire Resource A...");
                System.out.println("[Client " + clientId + "] Waiting for Resource A...");
                
                synchronized (resourceA) {
                    System.out.println("[Client " + clientId + "] ✓ Acquired Resource A");
                    out.println("Acquired Resource A");
                    out.println("SUCCESS: Both resources acquired!");
                    
                    // Simulate work with both resources
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                    
                    System.out.println("[Client " + clientId + "] Released Resource A");
                }
                System.out.println("[Client " + clientId + "] Released Resource B");
            }
            out.println("Resources released. Ready for next command.");
        }

        private void cleanup() {
            try {
                if (in != null) in.close();
                if (out != null) out.close();
                if (socket != null) socket.close();
                System.out.println("Client " + clientId + " disconnected\n");
            } catch (IOException e) {
                System.err.println("Error closing client " + clientId + ": " + e.getMessage());
            }
        }
    }
}
