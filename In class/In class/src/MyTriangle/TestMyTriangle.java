package MyTriangle;

public class TestMyTriangle {
    public static void main(String[] args) {
        //用mypoint对象定义点
        MyPoint p1 = new MyPoint(0, 0);
        MyPoint p2 = new MyPoint(2, 0);
        MyPoint p3 = new MyPoint(1, 1.7321);
        MyTriangle t1 = new MyTriangle(p1, p2, p3);
        System.out.println("Triangle t1:" + t1);
        System.out.println("Perimeter: " + t1.getPerimeter());
        System.out.println("Type: " + t1.printType()+"\n");
        //用坐标定义三角形的点
        MyTriangle t2 = new MyTriangle(0, 1, 0, 1, 0, 0);
        System.out.println("Triangle t2:" + t2);
        System.out.println("Perimeter: " + t2.getPerimeter());
        System.out.println("Type: " + t2.printType());
    }
}
