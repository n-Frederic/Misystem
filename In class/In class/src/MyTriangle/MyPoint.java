package MyTriangle;

import java.lang.Math;

public class MyPoint {
    private double x;
    private double y;

    /**
     * 构造器，构造一个在(0, 0)位置的点。
     */
    public MyPoint() {
        this.x = 0;
        this.y = 0;
    }

    /**
     * 构造器，创造一个给定坐标的点
     * @param x 给定点的x坐标
     * @param y 给定点的y坐标
     */
    public MyPoint(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * @return 返回x的坐标
     */
    public double getX() {
        return x;
    }

    /**
     * @return 返回y的坐标
     */
    public double getY() {
        return y;
    }

    /**
     * @param x x的坐标
     */
    public void setX(double x) {
        this.x = x;
    }

    /**
     * @param y y的坐标
     */
    public void setY(double y) {
        this.y = y;
    }

    /**
     * @param x 传入一个x坐标
     * @param y 传入一个y坐标
     */
    public void setXY(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * 以字符串形式获取坐标
     *
     * @return 字符串形式的（x,y）
     */
    public String toString() {
        return "(" + x + "," + y + ")";
    }

    /**
     * 计算当前点到给定坐标(x, y)的距离
     *
     * @param x 给定点的x坐标
     * @param y 给定点的y坐标
     * @return 当前点到给定点的距离
     */
    public double distance(int x, int y) {
        double x1 = this.x - x;
        double y1 = this.y - y;
        return Math.sqrt(x1 * x1 + y1 * y1);
    }

    /**
     * 计算当前点到另一个Mypoint实例的距离
     *
     * @param another 另一个Mypoint实例
     * @return 当前点到另一个Mypoint实例的距离
     */
    public double distance(MyPoint another) {
        double x2 = this.x - another.getX();
        double y2 = this.y - another.getY();
        return Math.sqrt(x2 * x2 + y2 * y2);
    }

    /**
     * 计算当前点到零点的距离
     * @return 当前点到（0,0）的距离
     */
    public double distance() {
        return distance(0, 0);
    }
}
