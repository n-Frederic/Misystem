package M11.Ex2;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import java.awt.*;

public class CalculatorGUI implements ActionListener {
    // Buttons and a place to put them.
    private Button key0, key1, key2, key3, key4;
    private Button key5, key6, key7, key8, key9;
    private Button keyequal, keyplus, keyminus;
    private Button keyperiod, keymult, keydiv;
    private Panel buttonArea;
    JButton clear = new JButton("归零");

    // Where answer will be displayed.
    private Label answer;

    public CalculatorGUI() {
        answer = new Label("0.0", Label.RIGHT);

        key0 = new Button("0");
        key1 = new Button("1");
        key2 = new Button("2");
        key3 = new Button("3");
        key4 = new Button("4");
        key5 = new Button("5");
        key6 = new Button("6");
        key7 = new Button("7");
        key8 = new Button("8");
        key9 = new Button("9");
        keyequal = new Button("=");
        keyplus = new Button("+");
        keyminus = new Button("-");
        keymult = new Button("*");
        keydiv = new Button("/");
        keyperiod = new Button(".");
        buttonArea = new Panel();

        key0.addActionListener(this);
        key1.addActionListener(this);
        key2.addActionListener(this);
        key3.addActionListener(this);
        key4.addActionListener(this);
        key5.addActionListener(this);
        key6.addActionListener(this);
        key7.addActionListener(this);
        key8.addActionListener(this);
        key9.addActionListener(this);
        keyequal.addActionListener(this);
        keyplus.addActionListener(this);
        keyminus.addActionListener(this);
        keymult.addActionListener(this);
        keydiv.addActionListener(this);
    }

    public void go() {
        buttonArea.setLayout(new GridLayout(4, 4));

        buttonArea.add(key7);
        buttonArea.add(key8);
        buttonArea.add(key9);
        buttonArea.add(keyplus);
        buttonArea.add(key4);
        buttonArea.add(key5);
        buttonArea.add(key6);
        buttonArea.add(keyminus);
        buttonArea.add(key1);
        buttonArea.add(key2);
        buttonArea.add(key3);
        buttonArea.add(keymult);
        buttonArea.add(key0);
        buttonArea.add(keyperiod);
        buttonArea.add(keyequal);
        buttonArea.add(keydiv);

        // Create a Frame
        JFrame f = new JFrame("Calculator");
        f.setSize(200, 200);

        f.add(answer, BorderLayout.NORTH);
        f.add(buttonArea, BorderLayout.CENTER);
        f.setVisible(true);

        //加一个清除键
        f.add(clear, BorderLayout.SOUTH);
        //关闭
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }

    Calculator calculator = new Calculator();

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();

        // 处理数字按钮
        if (Character.isDigit(command.charAt(0)) || command.equals(".")) {
                // 如果文本框不为空且最后一个字符是数字，则直接追加
                answer.setText(answer.getText() + command);
        }

        // 处理运算符按钮


        // 处理等号按钮
        else if (command.equals("=")) {
            String expression = answer.getText();
            String result = calculator.opEquals(expression);
            answer.setText(result); // 显示结果

        }
    }


    public static void main(String args[]) {
        CalculatorGUI calcGUI = new CalculatorGUI();
        calcGUI.go();
    }
}
