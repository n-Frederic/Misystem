package M11.Ex1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ChatClient extends JFrame{

    private JTextArea textArea;
    private JTextField textField;
    private JButton sendButton;
    private JButton quitButton;
    private JScrollPane scrollPane;

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

        //加监听器
        quitButton.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        System.exit(0);
                    }
                }
        );

        sendButton.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        String message = textField.getText();
                        textArea.append(message + "\n");
                        textField.setText(""); // 清空输入框，以便输入新消息
                    }
                }
        );

    }

    public void launchFrame() {
        // 创建窗口
        JScrollPane scrollPane = new JScrollPane(textArea);
        JFrame frame = new JFrame("Chat Room");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);

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

    }
}
