package ir.farzinnasiri.Client.menus.options.customize;

import ir.farzinnasiri.Utils.PlayerProperties;
import ir.farzinnasiri.Utils.Constants;
import ir.farzinnasiri.Utils.Timer;

import java.awt.*;

public class Presenter {
    private int x,y;
    private int shipWidth,shipHeight;
    private int index;
    private Timer timer;
    private PlayerProperties playerProperties;


    protected Presenter(PlayerProperties playerProperties){

        this.playerProperties = playerProperties;

        shipHeight = playerProperties.getSpaceship().getHeight();
        shipWidth = playerProperties.getSpaceship().getWidth();

        timer = new Timer();

        x = Constants.WIDTH/2;
        y = Constants.HEIGHT/2;

    }


    public void update() {
        if(timer.timeEvent(20)){
            if(index == 20){
                index = 0;
            }
            index += 1;
        }

    }

    public void draw(Graphics2D g2d) {

        g2d.drawImage(playerProperties.getSpaceship(),x,y,null);

        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,0.85f));
        g2d.drawImage(playerProperties.getEngine()[index],(int) x+shipWidth/2 -38,(int) y +shipHeight -25,80,150,null);
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,1));

    }
}
