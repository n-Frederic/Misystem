package client;

import java.io.*;
import java.net.*;

public class Client {

    private Socket socket;
    private BufferedReader in;

    public Client() {
        String serverAddress = "localhost";
        int port = 8080;
        try {
            // 连接到服务器
            socket = new Socket(serverAddress, port);
            System.out.println("Connected to server at " + serverAddress + ":" + port);

            // 获取输入流并包装为BufferedReader
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // 可以在这里启动一个线程来专门处理接收消息
            // 但为了简化，我们直接在主线程中处理
            receiveMessages();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 接收来自服务器的消息
    private void receiveMessages() {
        try {
            String message;
            while ((message = in.readLine()) != null) {
                // 处理接收到的消息
                System.out.println("Received from server: " + message);
            }
        } catch (IOException e) {
            e.printStackTrace();
            // 可以在这里处理断开连接的情况
            try {
                socket.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    // 发送消息到服务器（可选，根据需要添加）
    public void sendMessage(String message) {
        try (PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {
            out.println(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 关闭客户端连接（可选，通常在程序结束时调用）
    public void closeConnection() {
        try {
            if (in != null) {
                in.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Usage: Client <server_address> <port>");
            return;
        }

        String serverAddress = args[0];
        int port = Integer.parseInt(args[1]);

        Client client = new Client();
        client.sendMessage("Hello, server!");
    }
}