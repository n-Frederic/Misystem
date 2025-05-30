package entity;

import java.awt.Graphics;
import java.awt.Rectangle;

public class Wall {
    private int x; // 墙体的x坐标  
    private int y; // 墙体的y坐标  
    private int width; // 墙体的宽度  
    private int height; // 墙体的高度  

    public Wall(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    // 绘制墙体  
    public void draw(Graphics g) {
        g.fillRect(x, y, width, height); // 使用fillRect方法绘制一个矩形表示墙体  
    }

    // 获取墙体的矩形区域，用于碰撞检测  
    public Rectangle getRect() {
        return new Rectangle(x, y, width, height);
    }
}