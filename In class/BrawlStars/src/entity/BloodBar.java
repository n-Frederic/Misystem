package entity;

import java.awt.*;

import client.ui.Character;

public class BloodBar {
    private Character character;
    private static final int length = 60;
    private static final int height = 10;

    public BloodBar(Character character) {
        this.character = character;
    }

    public void draw(Graphics g) {
        int x = character.getX();
        int y = character.getY();
        int blood = character.getBlood();
        //底色白
        g.setColor(Color.white);
        g.drawRect(x + 10, y - 15, length, height);
        //血条颜色绿
        g.setColor(Color.GREEN);
        g.fillRect(x + 10, y - 15, blood*length/3, height);
        //边框黑
        g.setColor(Color.black);
        g.drawRect(x + 10, y - 15, length, height);

    }
}
