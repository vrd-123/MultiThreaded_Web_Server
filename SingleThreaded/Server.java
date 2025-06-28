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
            System.out.println("SingleThreaded Server is listening on port " + PORT);
            System.out.println("Press Ctrl+C to stop the server");

            while (running) {
                try {
                    Socket clientSocket = serverSocket.accept();
                    clientSocket.setSoTimeout(30000); // 30 second timeout
                    
                    System.out.println("Client connected: " + clientSocket.getInetAddress());
                    
                    try (
                        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))
                    ) {
                        out.println("Hello Client! -- from the SingleThreaded Server");

                        String response = in.readLine();
                        if (response != null) {
                            System.out.println("Client says: " + response);
                            out.println("You said: " + response);
                        }

                    } catch (IOException e) {
                        System.err.println("Client handler error: " + e.getMessage());
                    } finally {
                        try {
                            if (!clientSocket.isClosed()) {
                                clientSocket.close();
                            }
                        } catch (IOException e) {
                            System.err.println("Error closing socket: " + e.getMessage());
                        }
                    }
                } catch (IOException e) {
                    if (running) {
                        System.err.println("Error accepting client: " + e.getMessage());
                    }
                }
            }

        } catch (IOException e) {
            System.err.println("Server startup error: " + e.getMessage());
        }
    }
}
