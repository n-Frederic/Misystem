package server;

import java.net.ServerSocket;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class Server {
    private ServerSocket serverSocket;
    private List<ClientHandler> clients = new ArrayList<>();

    public Server(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        System.out.println("Server is running on port " + port);
        startListening();
    }

    private void startListening() {
        while (true) {
            try {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected from " + clientSocket.getInetAddress().getHostAddress());
                ClientHandler clientHandler = new ClientHandler(clientSocket, this);
                clients.add(clientHandler);
                clientHandler.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // 广播消息给所有客户端
    public void broadcastMessage(String message) {
        for (ClientHandler client : clients) {
            client.sendMessage(message);
        }
    }

    // 假设有一个简单的界面类来显示客户端信息
    public void updateClientListUI(String clientAddress) {
        // 这里应该是更新GUI的代码，但在这个示例中我们仅打印到控制台
        System.out.println("Client list updated: " + clientAddress + " joined.");
    }

    public static void main(String[] args) {
        try {
            new Server(12345);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
