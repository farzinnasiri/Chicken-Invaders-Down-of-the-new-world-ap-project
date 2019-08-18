package ir.farzinnasiri.Client.buttons;


import java.awt.*;
import java.awt.image.BufferedImage;

import ir.farzinnasiri.Client.audio.AudioManager;
import ir.farzinnasiri.Client.input.MouseInput;
import ir.farzinnasiri.Utils.Timer;


public class Button {

    protected BufferedImage mouseOutImg;
    protected BufferedImage mouseInImg;
    private boolean mouseIn;
    protected Rectangle boundingBox;
    private Action action;
    private Timer timer;
    private AudioManager audioManager;

    public Button(
            BufferedImage mouseOutImg,
            BufferedImage mouseInImg,
            int x, int y,
            Action action

    ) {
        audioManager = AudioManager.getInstance();
        timer = new Timer();
        this.mouseInImg = mouseInImg;
        this.mouseOutImg = mouseOutImg;
        boundingBox = new Rectangle(x, y, mouseOutImg.getWidth(), mouseOutImg.getHeight());
        this.action = action;
    }

    public void update() {

            mouseIn = boundingBox.contains(MouseInput.getX(), MouseInput.getY());

        if (!mouseIn && timer.timeEvent(70)) {
            MouseInput.setClicked(false);
        }


            if (mouseIn && MouseInput.isClicked()) {
                MouseInput.setClicked(false);
                audioManager.playSound("CLICK",true);
                action.doAction();
            }
    }

    public void draw(Graphics2D g2d) {

        if(mouseIn) {
            g2d.drawImage(mouseInImg, boundingBox.x, boundingBox.y, null);
        }else {
            g2d.drawImage(mouseOutImg, boundingBox.x, boundingBox.y,null);

        }




    }

}
