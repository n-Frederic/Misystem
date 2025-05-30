package SL275.M3.Ex1;
public class MyPoint {
    public int x;
    public int y;

    public String toString() {
        return ("[" + x + "," + y + "]");
    }

    //构造器
    public MyPoint(int x, int y) {
        this.x = x;
        this.y = y;
    }

    //设置坐标（x,y）
    public void setXY(int x, int y) {
        this.x = x;
        this.y = y;
    }
}

