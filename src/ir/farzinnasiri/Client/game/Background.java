package ir.farzinnasiri.Client.game;


import ir.farzinnasiri.Utils.Assets;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Background {
    private BufferedImage image;

    private int x;
    private int y;
    private int speed;
    //if direction is 0,the background moves vertically.if it is 1 the background moves horizontally
    private byte direction;

    public Background() {
        this(0,0);
    }

    public Background(int x, int y) {
        this.x = x;
        this.y = y;
        speed = 1;
        direction = 0;

        setImage(Assets.getInstance().getBackground("GAME_BACKGROUND"));

    }

    /**
     * Method that draws the image onto the Graphics object passed
     * @param window
     */
    public void draw(Graphics2D window) {

        // Draw the image onto the Graphics reference
        window.drawImage(image, getX(), getY(), image.getWidth(), image.getHeight(), null);
        if(direction == 0 ){
            moveVertical();
        }
        if(direction == 1){
            moveHorizontal();
        }


    }

    private void moveVertical(){
        this.y += speed;


        if (this.y >= image.getHeight()) {

            this.y = this.y - image.getHeight() * 2;
        }

    }

    private void moveHorizontal(){
        this.x += speed;


        if (this.x >= image.getWidth()) {

            this.x = this.x - image.getHeight() * 2;
        }

    }
    public void setDirection(int d){
        this.direction = (byte)d;
    }

    public void setSpeed(int speed){
        this.speed = speed;

    }
    public void setImage(BufferedImage image) {
        this.image = image;

    }

    public void setX(int x) {
        this.x = x;
    }
    public int getX() {
        return this.x;
    }
    public int getY() {
        return this.y;
    }
    public int getImageWidth() {
        return image.getWidth();
    }
    public int getImageHeight(){
        return image.getHeight();
    }


}