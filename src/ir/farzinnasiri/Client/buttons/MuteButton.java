package ir.farzinnasiri.Client.buttons;

import ir.farzinnasiri.Client.audio.AudioManager;
import ir.farzinnasiri.Client.input.MouseInput;

import java.awt.*;
import java.awt.image.BufferedImage;

public class MuteButton extends Button{
    private AudioManager audioManager;
    private Boolean mute;

    public MuteButton(BufferedImage mouseOutImg, BufferedImage mouseInImg, int x, int y, Action action) {
        super(mouseOutImg, mouseInImg, x, y, action);
        audioManager = AudioManager.getInstance();
        mute = audioManager.getMute();
    }

    @Override
    public void update(){
        if(MouseInput.isClicked() && boundingBox.contains(MouseInput.getX(),MouseInput.getY())){

            mute = !audioManager.getMute();
            audioManager.setMute(mute);

            MouseInput.setClicked(false);
        }


    }

    @Override
    public void draw(Graphics2D g2d){
        if(mute){
            g2d.drawImage(mouseInImg,boundingBox.x,boundingBox.y,null);
        }else{
            g2d.drawImage(mouseOutImg, boundingBox.x, boundingBox.y, null);
        }
    }


}
