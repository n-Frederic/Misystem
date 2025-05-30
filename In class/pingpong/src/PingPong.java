import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Set;
import java.util.HashSet;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.swing.*;

public class PingPong extends JPanel implements KeyListener, Runnable {
    private static final long serialVersionUID = 1L;
    private Set<Integer> keysPressed = new HashSet<>();
    private int paddle1Y = 0;
    private int paddle2Y = 0;
    private int ballXDirection = 1;
    private int ballYDirection = -1;
    private int player1Score = 0;
    private int player2Score = 0;
    private boolean gameRunning = true;
    private final int BALL_SIZE = 20;
    private final int PADDLE_WIDTH = 10;
    private final int PADDLE_HEIGHT = 100;
    private final int PADDLE_SPEED = 10;
    private final int SCORE_TO_WIN = 11;
    private final int FRAME_WIDTH = 600;
    private final int FRAME_HEIGHT = 600;
    private final int PADDING = 10;
    private final int BALL_SPEED = 1;
    private final int DELAY = 10;
    private int ballX = FRAME_WIDTH / 2 - BALL_SIZE / 2;;
    private int ballY = FRAME_HEIGHT / 2 - BALL_SIZE / 2;
    public boolean startScreen=true;

    public PingPong() {
        JFrame frame = new JFrame("Ping Pong");
        frame.setSize(FRAME_WIDTH+15, FRAME_HEIGHT+35);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.addKeyListener(this);
        frame.add(this);
        frame.setVisible(true);
        frame.setLocation(500,100);
        new Thread(this).start();
    }

    public void paint(Graphics g) {
        super.paint(g);
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, FRAME_WIDTH, FRAME_HEIGHT);


        if (startScreen) {
            // Draw start screen message
            g.setColor(Color.white);
            Font font = new Font("Arial", Font.BOLD, 36);
            g.setFont(font);
            g.drawString("Press SPACE to start", FRAME_WIDTH/2 -190, FRAME_HEIGHT/2);
        }
        else{
            g.setColor(Color.WHITE);
            g.fillRect(PADDING, paddle1Y, PADDLE_WIDTH, PADDLE_HEIGHT);
            g.fillRect(FRAME_WIDTH - PADDING - PADDLE_WIDTH, paddle2Y, PADDLE_WIDTH, PADDLE_HEIGHT);
            g.fillOval(ballX, ballY, BALL_SIZE, BALL_SIZE);
            Font font = new Font("Arial", Font.BOLD, 20);
            g.setFont(font);
            g.drawString("Player 1: " + player1Score, PADDING, PADDING+10);
            g.drawString("Player 2: " + player2Score, FRAME_WIDTH - PADDING - 100, PADDING+10);
        }
    }

    public void run() {
        while (gameRunning) {
            if (startScreen) {
                // Draw start screen message
                Graphics g = getGraphics();
                g.setColor(Color.white);
                g.drawString("Press SPACE to start", FRAME_WIDTH/2 - 70, FRAME_HEIGHT/2);
                g.dispose();

                // Wait for spacebar to be pressed
                while (startScreen) {
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            moveBall();
            movePaddles();
            checkCollisions();
            checkWin();
            repaint();
            try {
                Thread.sleep(DELAY);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void moveBall() {
        ballX += ballXDirection * BALL_SPEED;
        ballY += ballYDirection * BALL_SPEED;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        keysPressed.add(keyCode);
        if (keysPressed.contains(KeyEvent.VK_SPACE)) {
            startScreen = false;
        }
    }

    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();
        keysPressed.remove(keyCode);
    }
    private void movePaddles() {
        if (keysPressed.contains(KeyEvent.VK_W)) {
            paddle1Y -= PADDLE_SPEED;
        }
        if (keysPressed.contains(KeyEvent.VK_S)) {
            paddle1Y += PADDLE_SPEED;
        }
        if (keysPressed.contains(KeyEvent.VK_UP)) {
            paddle2Y -= PADDLE_SPEED;
        }
        if (keysPressed.contains(KeyEvent.VK_DOWN)) {
            paddle2Y += PADDLE_SPEED;
        }

        // Clamp paddle positions to screen bounds
        paddle1Y = Math.max(PADDING, Math.min(paddle1Y, FRAME_HEIGHT - PADDLE_HEIGHT - PADDING));
        paddle2Y = Math.max(PADDING, Math.min(paddle2Y, FRAME_HEIGHT - PADDLE_HEIGHT - PADDING));
    }

    private void checkCollisions() {
        if (ballY <= 0 || ballY >= FRAME_HEIGHT - BALL_SIZE) {
            ballYDirection *= -1;
        }
        if (ballX <= PADDING + PADDLE_WIDTH && ballY >= paddle1Y && ballY <= paddle1Y + PADDLE_HEIGHT) {
            ballXDirection *= -1;
            ballXDirection += ballXDirection > 0 ? 1 : -1;
            ballYDirection += ballYDirection > 0 ? 1 : -1;
        }
        if (ballX >= FRAME_WIDTH - PADDING - PADDLE_WIDTH - BALL_SIZE && ballY >= paddle2Y && ballY <= paddle2Y + PADDLE_HEIGHT) {
            ballXDirection *= -1;
            ballXDirection += ballXDirection > 0 ? 1 : -1;
            ballYDirection += ballYDirection > 0 ? 1 : -1;
        }
        if (ballX <= 0) {
            player2Score++;
            resetBall();
            gameRunning = false;
        }
        if (ballX >= FRAME_WIDTH - BALL_SIZE) {
            player1Score++;
            resetBall();
            gameRunning = false;
        }
        if (player1Score >= SCORE_TO_WIN || player2Score >= SCORE_TO_WIN) {
            gameRunning = false;
        }
        if (!gameRunning) {

            // Draw scores
            Graphics g = getGraphics();
            g.setColor(Color.white);
            Font font = new Font("Arial", Font.BOLD, 36);
            g.setFont(font);
            String scoreString = "Player 1: " + player1Score + "   Player 2: " + player2Score;
            int stringWidth = g.getFontMetrics().stringWidth(scoreString);
            g.drawString(scoreString, FRAME_WIDTH/2 - stringWidth/2, FRAME_HEIGHT/2);
            g.drawString("Press SPACE to continue",FRAME_WIDTH/2 - stringWidth/2-10, FRAME_HEIGHT/2-100);
            // Wait for space bar to be pressed
            while (!keysPressed.contains(KeyEvent.VK_SPACE)) {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            // Reset game and continue
            keysPressed.remove(KeyEvent.VK_SPACE);
            gameRunning = true;
        }
    }

    private void checkWin() {
        if (player1Score >= SCORE_TO_WIN || player2Score >= SCORE_TO_WIN) {
            gameRunning = false;
            JFrame frame = new JFrame("Game Over");
            frame.setSize(300, 300);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.add(new GameOverPanel(player1Score, player2Score));
            frame.setVisible(true);
            frame.setLocation(400,300);
        }
    }

    private void resetBall() {
        ballX = FRAME_WIDTH / 2 - BALL_SIZE / 2;
        ballY = FRAME_HEIGHT / 2 - BALL_SIZE / 2;
        ballXDirection = new Random().nextInt(2) == 0 ? -1 : 1;
        ballYDirection = new Random().nextInt(2) == 0 ? -1 : 1;
    }

    public static void main(String[] args) {
        new PingPong();
    }
}