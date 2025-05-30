package client.ui;

import java.awt.*;

public class Character {

    //角色的坐标
    private int x;
    private int y;
    //朝向和速度
    private Dir direction = Dir.DOWN;
    private static final int speed = 5;
    private boolean moving = false;
    //生命
    private boolean alive = true;
    private int blood;


    //构造方法
    public Character(int x, int y, Dir direct) {
        this.x = x;
        this.y = y;
        this.direction = direct;
        this.alive = true;
        this.blood = 3;
    }

//    //角色碰墙回退的方法
//    public void back() {
//        switch (direction) {
//            case 0:
//                y += speed;
//                break;
//            case 1:
//                y -= speed;
//                break;
//            case 2:
//                x += speed;
//                break;
//            case 3:
//                x -= speed;
//                break;
//        }
//    }

    public void paint(Graphics g) {
        g.setColor(Color.MAGENTA);
        g.fillRect(x, y, 50, 50);
        move();

    }

    public void move() {
        if (!moving) return;
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

    //获取角色的矩形区域，用于碰撞检测
    public Rectangle getRect() {
        return new Rectangle(getX(), getY(), 80, 80);
    }

    public boolean isMoving() {
        return moving;
    }

    public void setMoving(boolean moving) {
        this.moving = moving;
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public int getBlood() {
        return blood;
    }

    public void setBlood(int blood) {
        this.blood = blood;
    }

    public Dir getDirection() {
        return direction;
    }

    public void setDirection(Dir direction) {
        this.direction = direction;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }
}