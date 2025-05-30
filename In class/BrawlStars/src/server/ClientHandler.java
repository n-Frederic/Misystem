package server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

// 客户端处理类，负责处理与单个客户端的通信
class ClientHandler extends Thread {
    private Socket socket;
    private PrintWriter out;
    private Server server;

    public ClientHandler(Socket socket, Server server) throws IOException {
        this.socket = socket;
        this.server = server;
        this.out = new PrintWriter(socket.getOutputStream(), true);

        // 假设在构造函数中更新服务器界面
        server.updateClientListUI(socket.getInetAddress().getHostAddress());
    }

    @Override
    public void run() {
        // 这里可以添加接收客户端消息的逻辑
        // 但为了简化，我们仅保持线程运行
        try {
            while (true) {
                // 模拟工作，实际应用中应等待接收消息
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // 发送消息给客户端
    public void sendMessage(String message) {
        out.println(message);
    }
}