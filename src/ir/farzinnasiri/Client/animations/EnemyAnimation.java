package ir.farzinnasiri.Client.animations;


import ir.farzinnasiri.Utils.Assets;
import ir.farzinnasiri.Utils.Timer;

import java.awt.*;
import java.awt.image.BufferedImage;

public class EnemyAnimation  {
    private BufferedImage[] frames;
    private int i;
    private byte sign;
    private Timer flappingTimer;
    private int fps;


    public EnemyAnimation(String type,int fps){
        frames = Assets.getInstance().getEnemy(type);
        flappingTimer = new Timer();
        i = 0;
        this.fps = fps;
    }

    public void update() {
        if(flappingTimer.timeEvent(1000/fps)) {
            if (i == frames.length - 1) {
                sign = -1;
            } else if (i == 0) {
                sign = 1;
            }
            i += sign;
        }
    }



    public void draw(Graphics2D g2d) {


    }

    public BufferedImage getFrame(){
        return frames[i];

    }

}
