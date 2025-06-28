import java.io.*;
import java.net.*;
import java.util.concurrent.*;

public class Server {
    private static final int PORT = 8010;
    private static final int THREAD_POOL_SIZE = 20;
    private static final int QUEUE_CAPACITY = 100;
    private static volatile boolean running = true;
    private static ExecutorService executor;

    public static void main(String[] args) {
        // Create a bounded thread pool with a work queue
        executor = new ThreadPoolExecutor(
            THREAD_POOL_SIZE,                    // Core pool size
            THREAD_POOL_SIZE,                    // Maximum pool size
            60L,                                 // Keep alive time
            TimeUnit.SECONDS,                    // Time unit
            new ArrayBlockingQueue<>(QUEUE_CAPACITY), // Work queue
            new ThreadPoolExecutor.CallerRunsPolicy() // Rejection policy
        );

        // Add shutdown hook for graceful shutdown
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("\nShutting down server...");
            running = false;
            executor.shutdown();
            try {
                if (!executor.awaitTermination(10, TimeUnit.SECONDS)) {
                    executor.shutdownNow();
                }
            } catch (InterruptedException e) {
                executor.shutdownNow();
            }
        }));

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            serverSocket.setReuseAddress(true);
            System.out.println("ThreadPool Server is listening on port " + PORT);
            System.out.println("Thread pool size: " + THREAD_POOL_SIZE);
            System.out.println("Queue capacity: " + QUEUE_CAPACITY);
            System.out.println("Press Ctrl+C to stop the server");

            while (running) {
                try {
                    Socket clientSocket = serverSocket.accept();
                    clientSocket.setSoTimeout(30000); // 30 second timeout
                    
                    System.out.println("New client connected: " + clientSocket.getInetAddress() + 
                                     " (Active threads: " + ((ThreadPoolExecutor) executor).getActiveCount() + ")");
                    
                    executor.submit(new ClientHandler(clientSocket));
                    
                } catch (IOException e) {
                    if (running) {
                        System.err.println("Error accepting client: " + e.getMessage());
                    }
                }
            }

        } catch (IOException e) {
            System.err.println("Server error: " + e.getMessage());
        } finally {
            if (executor != null && !executor.isShutdown()) {
                executor.shutdown();
            }
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
            out.println("Hello Client! -- from the ThreadPool Server");
            
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
