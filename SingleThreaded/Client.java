import java.io.*;
import java.net.*;

public class Client {
    public void run() throws IOException {
        int port = 8010;
        InetAddress address = InetAddress.getByName("localhost");
        Socket socket = new Socket(address, port);

        PrintWriter toSocket = new PrintWriter(socket.getOutputStream(), true);
        toSocket.println("Hello from client");

        BufferedReader fromSocket = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        System.out.println("Response: " + fromSocket.readLine());

        toSocket.close();
        fromSocket.close();
        socket.close();
    }

    public static void main(String[] args) {
        Client client = new Client();
        try {
            client.run();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
