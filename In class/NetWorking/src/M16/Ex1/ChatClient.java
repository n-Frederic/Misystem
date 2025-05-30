package M16.Ex1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;

public class ChatClient extends JFrame {
    public static void main(String[] args) {
        ChatClient client = new ChatClient();
        client.launchFrame();
    }

    private JTextArea textArea;
    private JTextField textField;
    private JButton sendButton;
    private JButton quitButton;
    private JScrollPane scrollPane;
    private Socket connection;
    private BufferedWriter serverOut;
    private BufferedReader serverIn;
    private JComboBox comboBox;

    public ChatClient() {

        textArea = new JTextArea(15, 50);

        //滚动框，设置水平竖直都可以滚动
        scrollPane = new JScrollPane(textArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

        //创建单行输入框
        textField = new JTextField(50);

        //创建按钮
        sendButton = new JButton("Send");
        quitButton = new JButton("Quit");

        //quit监听器
        quitButton.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        System.exit(0);
                    }
                }
        );

        SendListener sendListener = new SendListener();
        sendButton.addActionListener(sendListener);

    }

    public void launchFrame() {

        // 创建窗口
        JScrollPane scrollPane = new JScrollPane(textArea);
        JFrame frame = new JFrame("Chat Room");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);

        //创建菜单栏和子项
        JMenuBar menuBar = new JMenuBar();
        frame.setJMenuBar(menuBar);
        JMenu menu = new JMenu("Menu");
        menuBar.add(menu);
        JMenuItem fileItem = new JMenuItem("File");
        menu.add(fileItem);
        JMenu subMenu = new JMenu("Help");
        JMenuItem aboutItem = new JMenuItem("About");
        subMenu.add(aboutItem);
        menu.add(subMenu);

        //about的监听器
        aboutItem.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        JDialog dialog = new JDialog(ChatClient.this, "关于聊天室", Dialog.ModalityType.APPLICATION_MODAL);
                        dialog.setTitle("About");
                        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                        dialog.setSize(300, 200);
                        JLabel aboutLabel = new JLabel("这是一个简单的聊天室程序");
                        dialog.getContentPane().add(aboutLabel, BorderLayout.CENTER);
                        aboutLabel.setVisible(true);
                        dialog.setVisible(true);
                    }
                }
        );

        //创建面板以管理布局
        JPanel mainPanel = new JPanel(new BorderLayout());
        //文本区域放在中间
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // 按钮面板用箱式布局
        JPanel buttonPanel = new JPanel();
        BoxLayout b = new BoxLayout(buttonPanel, BoxLayout.Y_AXIS);
        buttonPanel.setLayout(b);
        buttonPanel.add(sendButton);
        //加点空隙
        buttonPanel.add(Box.createVerticalStrut(15));
        buttonPanel.add(quitButton);
        //添加选项下拉栏

        String[] options = {"跳跳蛙", "红袋鼠", "火帽子"};
        comboBox = new JComboBox(options);
        buttonPanel.add(comboBox);


        //整体采用边框布局管理器
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(textField, BorderLayout.CENTER);

        //按钮放主面板右侧
        mainPanel.add(buttonPanel, BorderLayout.EAST);
        //将底部面板放在主面板的底部
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        //将主面板添加到窗口的内容面板中
        frame.getContentPane().add(mainPanel);
        //显示窗口
        frame.setVisible(true);

        //connection = null;
        try {
            Socket connection = new Socket("127.0.0.1", 6666);
            //创建客户端输入输出流
            serverOut = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));
            serverIn = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        //启动监听服务器消息的线程
        RemoteReader r = new RemoteReader();
        Thread t = new Thread(r);
        t.start();

        //显示窗口
        frame.setVisible(true);
    }

    private class RemoteReader implements Runnable {
        @Override
        public void run() {
            try {
                while (true) {
                    // do stuff here to continuously read from the server using the
                    // input stream you made
                    // put the received text into the text area
                    String message;
                    while (((message=serverIn.readLine()) != null)) {
                        textArea.append(message + "\n");
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // the inner class listening for the send button
    private class SendListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            new Thread(() -> {
                // get info from text field
                String prefix = (String) comboBox.getSelectedItem(); // 获取当前选择的项
                String message = textField.getText();
                // send it to the server using the printstream object
                String combinedMessage = prefix + ": " + message + "\n";
                try {
                    serverOut.write(combinedMessage);
                    serverOut.flush();

                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                // 清空TextField的操作在EDT上执行
                SwingUtilities.invokeLater(() -> {
                    textField.setText("");
                });
            }).start();
        }
    }

}
