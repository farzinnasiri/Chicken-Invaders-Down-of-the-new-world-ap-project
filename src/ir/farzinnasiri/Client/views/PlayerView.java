package ir.farzinnasiri.Client.views;

import ir.farzinnasiri.Utils.Assets;
import ir.farzinnasiri.Utils.Constants;
import ir.farzinnasiri.Utils.Timer;

import java.awt.*;
import java.awt.image.BufferedImage;

public class PlayerView extends DrawableObject {
    private String name;
    private boolean shied;



    private int shieldDuration;
    private Timer shieldTimer;
    private DrawableObject exhaust;
    private BufferedImage shieldImage;
    private long startingTime;
    private boolean lost;



    public PlayerView(int id, String name, String kind, int x, int y, String exhaustColor) {
        super(id, "SPACESHIP", kind, x, y);
        this.name = name;

        exhaust = new DrawableObject(id, "EXHAUST", exhaustColor, getX() + Constants.SPACESHIP_WIDTH / 2 - 38,
                getY() + Constants.SPACESHIP_HEIGHT - 20);

        shieldImage = Assets.getInstance().getOtherImageAssets("SHIELD");

        shieldTimer = new Timer();

        startingTime = System.currentTimeMillis();

    }


    @Override
    public void draw(Graphics2D g2d) {
        super.draw(g2d);
        if (shied) {
            if (shieldTimer.timeEvent(shieldDuration)) {
                shied = false;
            }
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.6f));
            g2d.drawImage(shieldImage,(int)getX()-(shieldImage.getWidth()-Constants.SPACESHIP_WIDTH)/2,
                    (int)getY()-(shieldImage.getHeight()-Constants.SPACESHIP_HEIGHT)/2,null );
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));

        } else {

            exhaust.setX((int) (getX() + Constants.SPACESHIP_WIDTH / 2 - 38));
            exhaust.setY((int) (getY() + Constants.SPACESHIP_HEIGHT - 20));
            exhaust.draw(g2d);


        }


        if (name != null) {
            g2d.setFont(Assets.getInstance().getFont("MAIN").deriveFont(14f));
            FontMetrics fm = g2d.getFontMetrics();
            g2d.setColor(Color.white);
            g2d.drawString(name, (int) (getX() + Constants.SPACESHIP_WIDTH / 2
                    - fm.getStringBounds(name, g2d).getCenterX()), (int) getY() - 10);
        }

    }

    public String getName() {
        return name;
    }

    public void playShield(int duration) {
        shied = true;
        shieldDuration = duration;
        shieldTimer.resetTimer();

    }

    public long getInGameTime(){
        return (System.currentTimeMillis()-startingTime)/1000;
    }

    public boolean isShied() {
        return shied;
    }
    public boolean isLost() {
        return lost;
    }

    public void setLost(boolean lost) {
        this.lost = lost;
    }
}
