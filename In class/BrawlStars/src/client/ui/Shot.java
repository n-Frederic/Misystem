package client.ui;

import entity.*;

import java.awt.*;
import java.util.ArrayList;

public class Shot implements Runnable {
    private int x;
    private int y;
    private static final int speed = 15;
    private Dir direction;
    private boolean alive = true;
    private Rectangle bulletRect;

    public Shot(int x, int y, Dir direction) {
        this.x = x;
        this.y = y;
        this.direction = direction;
        this.bulletRect = new Rectangle(x, y, 4, 4);
    }

    public void paint(Graphics g) {
        g.setColor(Color.white);
        g.fillRect(x, y, 4, 4);
        move();
    }
    public void move() {
        switch (direction) {
            case UP:
                y -= speed;
                break;
            case DOWN:
                y += speed;
                break;
            case LEFT:
                x -= speed;
                break;
            case RIGHT:
                x += speed;
                break;
        }
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public boolean isAlive() {
        return alive;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //根据角色方向判断子弹的方向
            switch (direction) {
                case 0:
                    y -= speed;
                    break;
                case 1:
                    y += speed;
                    break;
                case 2:
                    x -= speed;
                    break;
                case 3:
                    x += speed;
                    break;
            }
            //*测试
            // System.out.println("x: " + x + " y: " + y);

            //更新子弹的区域
            bulletRect.setLocation(x, y);
            //当子弹移动到界面的边缘，销毁它
            if (!(x <= 1000 && x >= 0 && y <= 1000 && y >= 0)) {
                alive = false;
                break;
            }
            //如果子弹碰到角色，销毁它

        }
    }

    //子弹和墙体碰撞消失
    public boolean hitWall(ArrayList<Wall> walls) {
        // 遍历所有的墙
        for (Wall eachWall : walls) {
            // 如果子弹的矩形与墙体的矩形相交
            if (this.alive && this.bulletRect.intersects(eachWall.getRect())) {
                // 处理碰撞，使子弹消失
                this.alive = false;
                return true; // 碰撞发生，立即返回true
            }
        }
        // 如果没有碰撞发生
        return false;
    }

    // 获取子弹的矩形区域，用于碰撞检测
    public Rectangle getBulletRect() {
        return bulletRect;
    }

}
