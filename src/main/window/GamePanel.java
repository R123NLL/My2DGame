package main.window;

import main.keyboard.KeyHandler;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel implements Runnable {

    //Screen settings
    final int originTilesSize = 16; // 16x16
    final int scale = 3;
    final int tileSize = originTilesSize * scale; // 48x48
    final int maxScreenCol = 16;
    final int maxScreenRow = 12;
    final int screenWidth = tileSize * maxScreenCol; //768 pixels
    final int screenHeight = tileSize * maxScreenRow; //576 pixels

    //FPS
    int fps = 60;

    KeyHandler keyHandler = new KeyHandler();
    Thread gameThread;

    int playerX = 100;
    int playerY = 100;
    int playerSpeed = 4;

    public GamePanel() {

        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyHandler);
        this.setFocusable(true);
    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {

        double drawInterval = 1000000000 / fps; // 0.01666 seconds
        double nextDrawTime = System.nanoTime() + drawInterval;

        while (gameThread != null) {

            // 1 UPDATE: update information such as character positions
            update();
            // 2 DRAW: draw the screen with updated information
            repaint();

            try {

                double remainingTime = nextDrawTime - System.nanoTime();

                remainingTime /= 1000000;

                if (remainingTime < 0) {
                    remainingTime = 0;
                }

                Thread.sleep((long) remainingTime);

                nextDrawTime += drawInterval;

            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void update() {
        if (keyHandler.upPressed == true) {
            playerY -= playerSpeed;
        }
        if (keyHandler.downPressed == true) {
            playerY += playerSpeed;
        }
        if (keyHandler.leftPressed == true) {
            playerX -= playerSpeed;
        }
        if (keyHandler.rightPressed == true) {
            playerX += playerSpeed;
        }
    }

    public void paintComponent(Graphics graphics) {

        super.paintComponent(graphics);

        Graphics2D graphics2D = (Graphics2D) graphics;

        graphics2D.setColor(Color.white);

        graphics2D.fillRect(playerX, playerY, tileSize, tileSize);

        graphics2D.dispose();
    }
}
