package M16.Ex1;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ChatServer {

    public static void main(String[] args) throws IOException {

        ServerSocket serverSocket = new ServerSocket(6666);
        System.out.println("Waiting for client...");

        new Thread(()-> {
            try {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Accepted connection from " + clientSocket.getRemoteSocketAddress());
                //输入输出流
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                BufferedWriter out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));

                String msg;
                //从一个客户端读取文本并返回文本
                while ((msg = in.readLine())!=null){
                    System.out.println("Received from client: " + msg);
                    out.write(msg + "\n"); // 添加换行符
                    out.flush();
                }

            }catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }
}