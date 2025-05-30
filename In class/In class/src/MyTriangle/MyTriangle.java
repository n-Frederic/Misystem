package MyTriangle;

public class MyTriangle {
    private MyPoint v1;
    private MyPoint v2;
    private MyPoint v3;

    //constructor that constructs a MyTriangle with three set of coordinates
    public MyTriangle(double x1, double x2, double x3, double y1, double y2, double y3) {
        v1 = new MyPoint(x1, y1);
        v2 = new MyPoint(x2, y2);
        v3 = new MyPoint(x3, y3);
    }

    //overloaded constructor that constructs a MyTriangle given three instances of MyPoint
    public MyTriangle(MyPoint v1, MyPoint v2, MyPoint v3) {
        this.v1 = v1;
        this.v2 = v2;
        this.v3 = v3;
    }

    //returns a string description of the instance in the format
    //"MyTriangle[v1=(x1,y1),v2=(x2,y2),v3=(x3,y3)]"
    public String toString() {
        return "MyTriangle[v1=" + v1 + ",v2=" + v2 + ",v3=" + v3;
    }

    //returns the length of the perimeter in double
    public double getPerimeter() {
        double ab = v1.distance(v2);
        double bc = v2.distance(v3);
        double ca = v3.distance(v1);
        return ab + bc + ca;
    }

    //Determine the type of the triangle
    public String printType() {
        double ab = v1.distance(v2);
        double bc = v2.distance(v3);
        double ca = v3.distance(v1);
        if (ab <= 0 || bc <= 0 || ca <= 0) {
            return "illegal input";
        } else if (ab - bc < 0.0001 && bc - ca < 0.0001 && ab - ca < 0.0001) {
            return "equilateral";
        } else if (ab == bc && bc == ca) {
            return "isosceles";
        } else return "scalene";
    }
}
