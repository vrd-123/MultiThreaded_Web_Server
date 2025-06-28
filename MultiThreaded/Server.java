import java.io.*;
import java.net.*;

public class Server {
    private static final int PORT = 8010;
    private static volatile boolean running = true;
    
    public static void main(String[] args) {
        // Add shutdown hook for graceful shutdown
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("\nShutting down server...");
            running = false;
        }));
        
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            serverSocket.setReuseAddress(true);
            System.out.println("MultiThreaded Server is listening on port " + PORT);
            System.out.println("Press Ctrl+C to stop the server");
            
            while (running) {
                try {
                    Socket clientSocket = serverSocket.accept();
                    clientSocket.setSoTimeout(30000); // 30 second timeout
                    
                    System.out.println("New client connected: " + clientSocket.getInetAddress());
                    
                    // Create and start a new thread for each client
                    Thread clientThread = new Thread(new ClientHandler(clientSocket));
                    clientThread.setDaemon(true); // Make thread daemon so it doesn't prevent shutdown
                    clientThread.start();
                    
                } catch (IOException e) {
                    if (running) {
                        System.err.println("Error accepting client: " + e.getMessage());
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Server error: " + e.getMessage());
        }
    }
}

class ClientHandler implements Runnable {
    private final Socket clientSocket;

    public ClientHandler(Socket socket) {
        this.clientSocket = socket;
    }

    @Override
    public void run() {
        try (
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))
        ) {
            // Send welcome message
            out.println("Hello Client! -- from the MultiThreaded Server");
            
            // Read client message
            String clientMessage = in.readLine();
            if (clientMessage != null) {
                System.out.println("Client [" + clientSocket.getInetAddress() + "] says: " + clientMessage);
                out.println("You said: " + clientMessage);
            }

        } catch (IOException e) {
            System.err.println("Client handler error for " + clientSocket.getInetAddress() + ": " + e.getMessage());
        } finally {
            try {
                if (!clientSocket.isClosed()) {
                    clientSocket.close();
                }
            } catch (IOException e) {
                System.err.println("Error closing socket: " + e.getMessage());
            }
        }
    }
}