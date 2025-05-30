package SL275.M3.Ex1;

import MyPoint.MyPoint;

public class TestMyPoint {
    public static void main(String[] args) {
        MyPoint start = new MyPoint(10, 10);
        System.out.println("Start point is " + start);
        MyPoint end = new MyPoint(20, 30);
        System.out.println("End point is " + end+"\n");

        MyPoint stray;
        stray = end;
        System.out.println("Stray point is " + stray);
        System.out.println("End point is " + end+"\n");

        stray.setXY(47, 30);
        System.out.println("Start point is " + start);
        System.out.println("Stray point is " + stray);
        System.out.println("End point is " + end);
    }
}
