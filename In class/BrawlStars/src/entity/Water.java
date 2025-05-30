package entity;

import java.awt.Graphics;
import java.awt.Rectangle;

public class Water {
    private int x;
    private int y;
    private int width;
    private int height;

    public Water(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    //绘制水面
    public void draw(Graphics g) {
        g.fillRect(x, y, width, height); // 使用fillRect方法绘制一个矩形表示墙体
    }

    //获取水面的矩形区域，用于碰撞检测
    public Rectangle getRect() {
        return new Rectangle(x, y, width, height);
    }
}