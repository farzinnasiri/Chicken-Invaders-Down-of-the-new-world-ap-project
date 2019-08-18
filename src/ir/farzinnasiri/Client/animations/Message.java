package ir.farzinnasiri.Client.animations;

import ir.farzinnasiri.Utils.Assets;
import ir.farzinnasiri.Utils.Vector2D;

import java.awt.*;



public class Message {
    private float alpha;
    private String text;
    private Color color;
    private Vector2D position;
    private boolean center;
    private boolean fade;
    private Font font;
    private final float deltaAlpha = 0.005f;
    private boolean dead;
    private FontMetrics fm;

    public Message(Vector2D position, boolean fade, String text, Color color,
                   boolean center) {
        this.font = Assets.getInstance().getFont("LEVEL").deriveFont(60f);
        this.text = text;
        this.position = position;
        this.fade = fade;
        this.color = color;
        this.center = center;
        this.dead = false;

        if(fade) {
            alpha = 1;
        }
        else {
            alpha = 0;
        }

    }

    public void draw(Graphics2D g2d) {

        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        g2d.setColor(color);
        g2d.setFont(font);

        fm = g2d.getFontMetrics();
        g2d.drawString(text,(int)position.getX()-fm.stringWidth(text)/2,(int)position.getY());
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,1));



        if(fade) {
            alpha -= deltaAlpha;
        }
        else {
            alpha += deltaAlpha;
        }

        if(fade && alpha < 0) {
            dead = true;
        }

        if(!fade && alpha > 1) {
            fade = true;
            alpha = 1;
        }

    }

    public boolean isDead() {return dead;}


}