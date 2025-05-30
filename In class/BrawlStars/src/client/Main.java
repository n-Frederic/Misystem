package client;

import client.ui.MapFrame;

import javax.swing.*;

public class Main{
    public static void main(String[] args) throws InterruptedException {
        MapFrame mf = new MapFrame();
        while (true){
            Thread.sleep(50);
            mf.repaint();
        }
    }
}
