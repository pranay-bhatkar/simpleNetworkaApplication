package client;

import java.io.*;
import java.net.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ChatClient extends JFrame {
    private JTextArea chatArea;
    private JTextField inputField;
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    public ChatClient() {
        // GUI setup
        chatArea = new JTextArea();
        chatArea.setEditable(false);
        inputField = new JTextField();
        inputField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });
        add(new JScrollPane(chatArea), BorderLayout.CENTER);
        add(inputField, BorderLayout.SOUTH);
        setTitle("Chat Client");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        // Client setup
        try {
            socket = new Socket("localhost", 12345);
            chatArea.append("Connected to server.\n");

            // Setup I/O streams
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // Handle server messages
            String message;
            while ((message = in.readLine()) != null) {
                chatArea.append("Server: " + message + "\n");
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeConnections();
        }
    }

    private void sendMessage() {
        String message = inputField.getText();
        out.println(message);
        chatArea.append("You: " + message + "\n");
        inputField.setText("");
    }

    private void closeConnections() {
        try {
            if (in != null)
                in.close();
            if (out != null)
                out.close();
            if (socket != null)
                socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new ChatClient();
    }
}
