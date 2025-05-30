//package client.ui;
//
//import entity.Wall;
//import entity.Water;
//
//import java.awt.*;
//import java.util.ArrayList;
//import java.util.Vector;
//
//public class skull extends Character {
//
//    Shot shot = null;
//    Vector<Shot> shots = new Vector<>();
//
//    public skull(int x, int y, int direct, int speed) {
//        super(x, y, direct, speed);
//    }
//
//    //根据自身的朝向发射子弹
//    public void shoot() {
//        switch (getDirect()) {
//            case 0:
//                shot = new Shot(getX() + 40, getY(), 0);
//                break;
//            case 1:
//                shot = new Shot(getX() + 40, getY() + 80, 1);
//                break;
//            case 2:
//                shot = new Shot(getX(), getY() + 40, 2);
//                break;
//            case 3:
//                shot = new Shot(getX() + 80, getY() + 40, 3);
//                break;
//        }
//        //每创建一个新的子弹就放到集合里
//        shots.add(shot);
//        //启动线程
//        new Thread(shot).start();
//    }
//
//    // 和水的碰撞检测方法
//    public boolean checkCollisionWithWater(ArrayList<Water> waters) {
//        Rectangle characterRect = new Rectangle(getX(), getY(), 80, 80);
//        for (Water w : waters) {
//            Rectangle wallRect = w.getRect();
//            if (characterRect.intersects(wallRect)) {
//                //确实碰到了
//                return true;
//            }
//        }
//        //没有与任何墙体碰撞
//        return false;
//    }
//
//    // 和墙的碰撞检测方法
//    public boolean checkCollisionWithWall(ArrayList<Wall> walls) {
//        Rectangle characterRect = new Rectangle(getX(), getY(), 80, 80);
//        for (Wall wall : walls) {
//            Rectangle wallRect = wall.getRect();
//            if (characterRect.intersects(wallRect)) {
//                //确实碰到了
//                return true;
//            }
//        }
//        //没有与任何墙体碰撞
//        return false;
//    }
//
//    // 和其他玩家的碰撞检测方法
//    public boolean checkCollisionWithCharacter(ArrayList<Character> characters) {
//        Rectangle myRect = new Rectangle(getX(), getY(), 80, 80);
//        for (Character c : characters) {
//            if (c != this) {
//                Rectangle otherRect = c.getRect();
//                if (myRect.intersects(otherRect)) {
//                    return true;
//                }
//            }
//        }
//        return false;
//    }
//
//    public boolean checkCollisionWithShot(Vector<Shot> shots) {
//        Rectangle characterRect = this.getRect(); // 注意这里使用this来引用当前角色的矩形
//        for (Shot s : shots) {
//            Rectangle bulletRect = s.getBulletRect();
//            if (characterRect.intersects(bulletRect)) {
//                // 如果当前角色与子弹碰撞，销毁子弹并返回true
//                s.setAlive(false);
//                return true;
//            }
//        }
//        return false; // 如果没有碰撞发生，返回false
//    }
//
//}
