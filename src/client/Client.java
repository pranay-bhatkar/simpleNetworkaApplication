
package client;

import java.io.*;
import java.net.*;
import java.util.Scanner;

class ClientThread extends Thread {
    private Socket socket;

    public ClientThread(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        try (InputStream input = socket.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(input))) {

            String serverMessage;
            while ((serverMessage = reader.readLine()) != null) {
                System.out.println(serverMessage);
            }
        } catch (IOException e) {
            System.out.println("Client exception: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

public class Client {
    private static final String HOSTNAME = "localhost";
    private static final int PORT = 12345;

    public static void main(String[] args) {
        try (Socket socket = new Socket(HOSTNAME, PORT)) {
            System.out.println("Connected to the server");

            new ClientThread(socket).start();

            try (OutputStream output = socket.getOutputStream();
                    PrintWriter writer = new PrintWriter(output, true);
                    Scanner scanner = new Scanner(System.in)) {

                String userMessage;
                do {
                    System.out.print("Enter message: ");
                    userMessage = scanner.nextLine();
                    writer.println(userMessage);
                } while (!userMessage.equalsIgnoreCase("exit"));
            }
        } catch (IOException e) {
            System.out.println("Client exception: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
