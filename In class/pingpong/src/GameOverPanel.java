import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
import java.awt.*;

class GameOverPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    private BufferedImage image;

    public GameOverPanel(int player1Score, int player2Score) {
        this.player1Score = player1Score;
        this.player2Score = player2Score;
        try {
            // Load the image file
            image = ImageIO.read(new File("C:/Users/timberman/Desktop/JianRan.jpg"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Draw the image in the center of the panel
        int x = (getWidth() - image.getWidth()) / 2;
        int y = (getHeight() - image.getHeight()) / 2;
        g.drawImage(image, x, y, null);
    }
    private int player1Score;
    private int player2Score;


    public void paint(Graphics g) {
        super.paint(g);
        Font font = new Font("Arial", Font.BOLD, 20);
        g.setFont(font);
        g.drawString("Game Over", 30, 30);
        if(player1Score>player2Score){
            g.drawString("player1 Win",30,50);
        }
        else{
            g.drawString("player2 Win",30,50);
        }
    }
}