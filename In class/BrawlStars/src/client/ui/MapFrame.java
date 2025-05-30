package client.ui;

import javax.swing.*;

public class MapFrame extends JFrame {
    private Game game;

    public MapFrame() {
        //创建窗体
        setTitle("Brawl Stars");
        setSize(1000, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.add(game = new Game());

//        game = new Game();
//
//        //把mapPanel放入线程，启动线程调用run方法开始不断重绘
//        Thread thread = new Thread(game);
//        thread.start();
//
//        this.add(game);
//        //监听器
//        this.addKeyListener(game);

        setVisible(true);
    }

}