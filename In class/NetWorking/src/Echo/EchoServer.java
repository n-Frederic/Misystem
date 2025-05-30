package Echo;

import java.io.*;
import java.net.*;

public class EchoServer {
    public static void main(String[] args) throws IOException {

        EchoClient echoClient = new EchoClient();
        echoClient.launchFrame();

        ServerSocket serverSocket;


        serverSocket = new ServerSocket(4448);
        System.out.println("wait for client request");

        while (true) {
            Socket clientSocket = serverSocket.accept();
            new Thread(() -> {
                try {
                    //输入输出流
                    BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

                    //读取客户端发送的行数
                    int linesToSend = Integer.parseInt(in.readLine());

                    // 读取文件并发送指定数量的文本
                    readAndSendFileLines("multiLines.txt", linesToSend, out);

                    //通知结束
                    out.println("EOF");

                } catch (IOException e) {
                    System.err.println("Accept failed.");
                }
            }).start();
        }
    }
    private static int lastLineSent = 0; // 静态变量来跟踪上次发送的行数
    private static void readAndSendFileLines(String fileName, int linesToSend, PrintWriter out) throws IOException {
        FileReader fr = new FileReader(fileName);
        BufferedReader br = new BufferedReader(fr);
        String line;
        int linesSent = 0;
        int startLine = lastLineSent; // 从上次发送的位置开始

        // 跳过已经发送的行
        for (int i = 0; i < startLine && (line = br.readLine()) != null; i++) {
            // 不做任何事情，只是跳过行
        }

        //不断读取文件并检测够不够
        while ((line = br.readLine()) != null && linesSent < linesToSend) {
            out.println(line);
            linesSent++;
        }
        lastLineSent = startLine + linesSent;
        if (linesSent < linesToSend) {
            // 通知客户端文件中没有足够的行
            out.println("No more sentences");
        }

        // 关闭文件读取器
        br.close();
    }
}