import javax.swing.*;

public class FirstSwingApp {
    public static void main(String[] args) {
        //创建窗体
        JFrame frame = new JFrame("First Swing");
        JPanel panel = new JPanel();
        frame.add(new JButton("OK"));
        frame.add(new JLabel("SOS"));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 800);
        frame.pack();
        frame.setVisible(true);
    }
}
