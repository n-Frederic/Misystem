package MyPoint;

public class TestMyPoint {
    public static void main(String args[]) {
        MyPoint a = new MyPoint(0, 0);
        a.setX(1);
        a.setY(1);
        System.out.println("当前点x坐标值"+a.getX());
        System.out.println("当前点y坐标值"+a.getY());
        a.setXY(4, 5);
        System.out.println("当前点坐标："+a.toString());
        System.out.println("距离给定点的距离:"+a.distance(4, 4));
        System.out.println("距离给定点的距离："+a.distance(4, 2));
        System.out.println("距离零点的距离："+a.distance());
    }
}
