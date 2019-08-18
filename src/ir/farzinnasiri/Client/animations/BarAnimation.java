package ir.farzinnasiri.Client.animations;


import ir.farzinnasiri.Utils.Vector2D;

import java.awt.*;
import java.awt.image.BufferedImage;

public class BarAnimation {
    private BufferedImage image;
    private Vector2D coordinates;
    private int vSpeed,hSpeed;
    private int finalX;
    private int finalY;

    public BarAnimation(BufferedImage image, Vector2D coordinates,int finalX,int finalY,int vSpeed, int hSpeed){
        this.coordinates =coordinates;
        this.image = image;
        this.vSpeed = vSpeed;
        this.hSpeed = hSpeed;
        this.finalX = finalX;
        this.finalY = finalY;
    }




    public void update() {
        coordinates.setX(coordinates.getX()+hSpeed);
        coordinates.setY(coordinates.getY()+vSpeed);


    }


        public void draw(Graphics2D g2d) {
        g2d.drawImage(image,(int) coordinates.getX(),(int) coordinates.getY(),null);

    }

    public boolean isPositioned(){
        return coordinates.getX() >= finalX && coordinates.getY() >= finalY;

    }
}
