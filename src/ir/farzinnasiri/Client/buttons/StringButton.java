package ir.farzinnasiri.Client.buttons;

import ir.farzinnasiri.Client.audio.AudioManager;
import ir.farzinnasiri.Client.input.MouseInput;
import ir.farzinnasiri.Utils.Assets;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class StringButton extends Button {
    private Action action;
    private String text;
    private int x,y;
    private Rectangle boundingBox;
    private Color color;
    private Font font;
    private boolean mouseIn;
    private AudioManager audioManager;

    public StringButton(String text,int x,int y,Action action){
        super(new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB),
                new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB),0,0,action);
        this.text = text;

        AffineTransform affinetransform = new AffineTransform();
        FontRenderContext frc = new FontRenderContext(affinetransform,true,true);
        font = Assets.getInstance().getFont("MAIN").deriveFont(30f);
        int textwidth = (int)(font.getStringBounds(text, frc).getWidth());
        int textheight = (int)(font.getStringBounds(text, frc).getHeight());

        audioManager = AudioManager.getInstance();

        this.x =x-textwidth/2;
        this.y = y;

        boundingBox = new Rectangle(x-textwidth/2,y-textheight,textwidth,textheight);
        
        this.action = action;
        color = Color.white;



    }


    @Override
    public void update() {
        if(boundingBox.contains(MouseInput.getX(),MouseInput.getY())){
            color = Color.YELLOW;
            mouseIn = true;
        }else{
            color = Color.WHITE;
            mouseIn = false;

        }

        if(mouseIn && MouseInput.isClicked()){
            MouseInput.setClicked(false);
            audioManager.playSound("CLICK",true);
            action.doAction();
        }




    }

    @Override
    public void draw(Graphics2D g2d) {
        g2d.setColor(color);
        g2d.setFont(font);
        g2d.drawString(text,x,y);


    }
}
