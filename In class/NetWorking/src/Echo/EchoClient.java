package Echo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.*;

public class EchoClient {
    private JButton button;
    private JTextArea textArea;
    private JTextField textField;
    private JLabel label;
    private int linesToSend;

    public EchoClient() {
        button = new JButton("send to server");
        textArea = new JTextArea(600, 350);
        textField = new JTextField(5);
        label = new JLabel("Please input the number of string lines to get from server");

        //添加监听器到按钮
        SendListener sendListener = new SendListener();
        button.addActionListener(sendListener);

    }

    public void launchFrame() {
        JFrame frame = new JFrame("Product System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        panel.add(label);
        panel.add(textField);
        panel.add(button);
        frame.add(panel, BorderLayout.NORTH);
        frame.add(textArea, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    // send按钮的监听器
    private class SendListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            // 从textField读需要输入的行数
            String linesStr = textField.getText();
            if (linesStr.isEmpty()) {
                //空输入
                return;
            }
            new Thread(() -> {
                try {
                    linesToSend = Integer.parseInt(linesStr);
                    //传到服务端，接收返回内容并添加
                    sendToServer(linesToSend);
                    //清空TextField
                    textField.setText("");
                } catch (NumberFormatException ex) {
                    //非数字输入
                    textArea.append("Please enter a valid number of lines.\n");
                }
            }).start();
        }

        //发送到服务端
        private void sendToServer(int lines) {

            try (Socket socket = new Socket("127.0.0.1", 4448);
                 PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                 BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

                // 把要读多少行传到服务端
                out.println(lines);

                // 获取服务端返回的内容
                String response;
                textArea.append("\nGet " + lines + " lines from server\n");
                while ((response = in.readLine()) != null && !"EOF".equals(response)) {
                    textArea.append(response);
                    if (response.contains("No more sentences")) {
                        textArea.append("\ncan not take sentences anymore");
                        break;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
