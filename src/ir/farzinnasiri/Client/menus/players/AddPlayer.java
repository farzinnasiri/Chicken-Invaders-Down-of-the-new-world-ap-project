package ir.farzinnasiri.Client.menus.players;



import ir.farzinnasiri.Client.buttons.Action;
import ir.farzinnasiri.Client.buttons.Button;
import ir.farzinnasiri.Client.systemManager.PlayersManager;
import ir.farzinnasiri.Utils.Assets;
import ir.farzinnasiri.Utils.Constants;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;

public class AddPlayer implements KeyListener {
    private Assets assets;
    private String name;
    private BufferedImage oKBtnIn;
    private BufferedImage oKBtnOut;
    private BufferedImage addingPlayerBox;
    private boolean consume;
    private String bar;
    private Button oKBtn;
    private boolean adding;


    AddPlayer(PlayersManager playersManager){
        assets = Assets.getInstance();

        addingPlayerBox = assets.getBox("NAMEINPUT");
        name = "";
        oKBtnIn = assets.getButton("OK_IN");
        oKBtnOut = assets.getButton("OK_OUT");
        consume = true;
        bar = "";

        oKBtn = new Button(oKBtnOut, oKBtnIn, (Constants.WIDTH - oKBtnIn.getWidth()) / 2,
                Constants.HEIGHT / 2 + 50, new Action() {
            @Override
            public void doAction() {
                if(name.length() >0){
                    playersManager.addNewPlayer(name);
                    playersManager.setCurrentPlayer(name);
                    adding = false;
                    name ="";
                }

            }
        });
    }

    public void update() {

        oKBtn.update();

        if( ((int) (System.nanoTime() * 0.0000000075) % 3) > 0){
            bar ="_";
        }
        else {
            bar = "";
        }

    }

    public void draw(Graphics2D g2d) {
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,1f));

        g2d.drawImage(addingPlayerBox,(Constants.WIDTH-addingPlayerBox.getWidth())/2,
                (Constants.HEIGHT-addingPlayerBox.getHeight())/2,null);

        g2d.setFont(assets.getFont("MAIN").deriveFont(30f));
        FontMetrics fm = g2d.getFontMetrics();
        g2d.drawString(name+bar,(Constants.WIDTH-fm.stringWidth(name))/2,
                (Constants.HEIGHT-fm.getHeight())/2);

        oKBtn.draw(g2d);

        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,0.8f));


    }

    public boolean isAdding() {
        return adding;
    }

    public void setAdding(boolean adding) {
        this.adding = adding;
    }

    @Override
    public void keyTyped(KeyEvent e) {
        if(!(e.getKeyCode() == KeyEvent.VK_ENTER)) {
            if (!consume) {
                if (name.length() > 0) {
                    name = name.substring(0, name.length() - 1);
                }
            }
            if (name.length() <= 16 && consume) {
                name += e.getKeyChar();
            }
        }


    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_BACK_SPACE){
            consume = false;

        }
        if(e.getKeyCode() == KeyEvent.VK_ESCAPE){
            setAdding(false);
            name = "";
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_BACK_SPACE){
            consume = true;

        }
        if(e.getKeyCode() == KeyEvent.VK_ESCAPE){
            name = "";
        }

    }
}
