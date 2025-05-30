//毒圈
package client.ui;

import entity.BloodBar;
import entity.Wall;
import entity.Water;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

//继承runnable让子弹不停重绘
public class Game extends JPanel{

    private Character cha1 = new Character(10, 10, Dir.DOWN);

    Shot shot = new Shot(300,300,Dir.DOWN);
    //private gunner cha1 = new gunner(10, 10, 0, 5);
//    private skull cha2 = new skull(900, 10, 0, 5);
//    private robot cha3 = new robot(500, 10, 0, 5);
//    private JLabel gunnerShape;

//    //墙
//    private ArrayList<Wall> walls = new ArrayList<>();
//    //水
//    private ArrayList<Water> waters = new ArrayList<>();
//    //角色
//    private ArrayList<Character> characters = new ArrayList<>();


    public Game() {
        //监听器
        this.addKeyListener(new Game.myKeyListener());

//        characters.add(cha1);
//        characters.add(cha2);
//        characters.add(cha3);
//
//        // 加载枪手的图片文件
//        ImageIcon gunnerImage = new ImageIcon("D:/IDEA-javaproject/In class/BrawlStars/resources/gunner.jpg");
//        // 把JLabel的图标设置为加载的图片
//        gunnerShape = new JLabel(gunnerImage);
//        gunnerShape.setBounds(cha1.getX(), cha1.getY(), 80, 80);
//        add(gunnerShape);
//        //添加监听器
//
//
//
//        //加载骷髅的图片文件
//        ImageIcon skullImage = new ImageIcon("D:/IDEA-javaproject/In class/BrawlStars/resources/skull.jpg");
//        JLabel skullImageLabel = new JLabel(skullImage);
//        skullImageLabel.setBounds(cha2.getX(), cha2.getY(), 80, 80);
//        add(skullImageLabel);
//
//        //加载机器人的图片文件
//        ImageIcon robotImage = new ImageIcon("D:/IDEA-javaproject/In class/BrawlStars/resources/robot.jpg");
//        JLabel robotImageLabel = new JLabel(robotImage);
//        skullImageLabel.setBounds(cha3.getX(), cha3.getY(), 80, 80);
//        add(robotImageLabel);
//
//        //初始化墙
//        walls.add(new Wall(150, 150, 150, 50));
//        walls.add(new Wall(250, 150, 50, 150));
//
//        walls.add(new Wall(150, 600, 150, 50));
//        walls.add(new Wall(250, 500, 50, 150));
//
//        walls.add(new Wall(700, 150, 150, 50));
//        walls.add(new Wall(700, 150, 50, 150));
//
//        walls.add(new Wall(700, 600, 150, 50));
//        walls.add(new Wall(700, 500, 50, 150));
//
//        walls.add(new Wall(450, 350, 100, 100));
//
//        //地图边缘
//        walls.add(new Wall(0, 0, 1, 750));
//        walls.add(new Wall(1000, 0, 1, 750));
//        walls.add(new Wall(0, 0, 1000, 1));
//        walls.add(new Wall(0, 750, 1000, 1));
//
//        //初始化水面
//        waters.add(new Water(450, 550, 100, 50));
//        waters.add(new Water(450, 200, 100, 50));
    }


    @Override
    //画图方法
    protected void paintComponent(Graphics g) {

        cha1.paint(g);
        shot.paint(g);

        //super.paintComponent(g);

        //填充背景
        g.setColor(MyColor.Tan4);
        g.fillRect(0, 0, 1000, 750);


//        //画出墙体（子弹和角色都不可以通过）
//        g.setColor(MyColor.Orange);
//        for (Wall wall : walls) {
//            wall.draw(g);
//        }
//        //画出水面（子弹可以通过，角色不可以通过）
//        g.setColor(MyColor.LightSkyBlue);
//        for (Water water : waters) {
//            water.draw(g);
//        }
//
//        //画出血条
//        if (cha1.isAlive() == true) {
//            BloodBar bar = new BloodBar(cha1);
//            bar.draw(g);
//        }
//        //遍历Vector，画出所有射击的子弹
//        g.setColor(Color.white);
//        for (int i = 0; i < cha1.shots.size(); i++) {
//            Shot newShot = cha1.shots.get(i);
//            if (newShot != null && newShot.isAlive() == true) {
//                g.fill3DRect(newShot.getX(), newShot.getY(), 4, 4, false);
//            } else {
//                //如果子弹已经被销毁，从Vector中移除
//                cha1.shots.remove(newShot);
//            }
//            if (newShot.hitWall(walls) == true) {
//                newShot.setAlive(false);
//            }
//        }

    }

    class myKeyListener extends KeyAdapter {
        //监听字符输出
        @Override
        public void keyTyped(KeyEvent e) {
        }

        //记录按键状态
        boolean BU = false;
        boolean BD = false;
        boolean BL = false;
        boolean BR = false;

        //按下触发，用WSAD键控制角色上下左右的移动
        @Override
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();
            switch (key) {
                case KeyEvent.VK_W:
                    BU = true;
                    break;
                case KeyEvent.VK_S:
                    BD = true;
                    break;
                case KeyEvent.VK_A:
                    BL = true;
                    break;
                case KeyEvent.VK_D:
                    BR = true;
                    break;
                default:
                    break;
            }
            setDirection();

//        //按下J发射子弹
//        if (e.getKeyCode() == KeyEvent.VK_J && cha1.isAlive() == true) {
//            cha1.shoot();
//        }
//        //重绘
//        repaint();
        }

        //松开触发
        @Override
        public void keyReleased(KeyEvent e) {
            int key = e.getKeyCode();
            switch (key) {
                case KeyEvent.VK_W:
                    BU = false;
                    break;
                case KeyEvent.VK_S:
                    BD = false;
                    break;
                case KeyEvent.VK_A:
                    BL = false;
                    break;
                case KeyEvent.VK_D:
                    BR = false;
                    break;
                default:
                    break;
            }
            setDirection();
        }

        private void setDirection() {
            if (!BU && !BD && !BL && !BR) {
                cha1.setMoving(false);
            } else {
                cha1.setMoving(true);

                if (BU) cha1.setDirection(Dir.UP);
                if (BD) cha1.setDirection(Dir.DOWN);
                if (BL) cha1.setDirection(Dir.LEFT);
                if (BR) cha1.setDirection(Dir.RIGHT);
            }
        }
    }


//    public void run() {
//        while (true) {
//            try {
//                Thread.sleep(25);
//            } catch (InterruptedException e) {
//                e.getStackTrace();
//            }
//            //随时检查有没有碰到墙、水和其他玩家
//            if (cha1.checkCollisionWithWall(walls) == true || cha1.checkCollisionWithWater(waters) == true
//                    || cha1.checkCollisionWithCharacter(characters) == true) {
//                cha1.back();
//            }
//            //如果碰到子弹，生命值减一，生命值减完则死亡
//            if (cha1.checkCollisionWithShot(cha1.shots) == true) {
//                cha1.setBlood(cha1.getBlood() - 1);
//                if (cha1.getBlood() <= 0) {
//                    cha1.setAlive(false);
//                    characters.remove(cha1);
//                    remove(gunnerShape);
//                }
//            }
//            //如果角色还活着，更新坐标
//            if (cha1.isAlive() == true) {
//                gunnerShape.setBounds(cha1.getX(), cha1.getY(), 80, 80);
//            }
//            this.repaint();
//        }
//    }
}