//package Map;
//
//import java.awt.*;
//
//public class ShrinkingCircle {
//    private float centerX, centerY; // 圆心坐标
//    private float radius; // 半径
//    private float shrinkRate; // 缩小速率（每秒缩小的像素数）
//
//    public ShrinkingCircle(float centerX, float centerY, float initialRadius, float shrinkRate) {
//        this.centerX = centerX;
//        this.centerY = centerY;
//        this.radius = initialRadius;
//        this.shrinkRate = shrinkRate;
//    }
//
//    public void update(float deltaTime) {
//        if (radius > 0) {
//            radius -= shrinkRate * deltaTime; // 根据时间差更新半径
//            if (radius <= 0) {
//                radius = 0; // 防止负半径
//            }
//        }
//    }
//
//    public void draw(Graphics2D g2d) {
//        if (radius > 0) {
//            g2d.setColor(Color.RED); // 毒圈颜色
//            g2d.fillOval((int) (centerX - radius), (int) (centerY - radius), (int) (radius * 2), (int) (radius * 2));
//        }
//    }
//
//
//}