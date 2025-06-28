import java.io.*;
import java.net.*;
import java.util.concurrent.*;

public class Client {
    private static final int NUM_CLIENTS = 100;
    private static final int PORT = 8010;
    
    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(NUM_CLIENTS);
        
        System.out.println("Starting " + NUM_CLIENTS + " concurrent clients...");
        
        for (int i = 0; i < NUM_CLIENTS; i++) {
            final int clientId = i;
            executor.submit(() -> {
                try {
                    connectClient(clientId);
                } catch (Exception e) {
                    System.err.println("Client " + clientId + " error: " + e.getMessage());
                }
            });
        }
        
        executor.shutdown();
        try {
            if (!executor.awaitTermination(30, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
        }
        
        System.out.println("All clients completed.");
    }
    
    private static void connectClient(int clientId) throws IOException {
        try (Socket socket = new Socket("localhost", PORT);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            
            String message = "Hello from Client " + clientId + " [" + socket.getLocalSocketAddress() + "]";
            out.println(message);
            
            String response = in.readLine();
            System.out.println("Client " + clientId + " received: " + response);
            
        } catch (IOException e) {
            System.err.println("Client " + clientId + " connection failed: " + e.getMessage());
            throw e;
        }
    }
}
