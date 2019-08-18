package ir.farzinnasiri.Client.menus.splashScreen;

import ir.farzinnasiri.Client.stateMachine.StateMachine;
import ir.farzinnasiri.Client.stateMachine.SuperStateMachine;
import ir.farzinnasiri.Client.systemManager.PlayersManager;
import ir.farzinnasiri.Utils.Assets;
import ir.farzinnasiri.Utils.Constants;
import ir.farzinnasiri.Utils.Timer;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;

public class SplashScreen extends SuperStateMachine implements KeyListener {
    private Timer timer;
    private float alpha;
    private String text;
    private boolean fading;
    private int index;
    private boolean pushSpaceToStartVisible;
    private boolean pressed;
    private PlayersManager playersManager;
    private Assets assets;
    private Cursor blankCursor;

    public SplashScreen(StateMachine stateMachine) {
        super(stateMachine);
        assets = Assets.getInstance();
        timer = new Timer();
        alpha = 1;
        index = 0;

        text = "F.N Games Presents";
        playersManager = PlayersManager.getInstance();
        Image i = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(
                i, new Point(0, 0), "blank cursor");


    }

    @Override
    public void update(double delta) {
        if(pressed && fading) {
            if(validatePlayers()){
            setState(StateMachine.MENU);

            }else {
                setState(StateMachine.PLAYERS);
            }
        }
        if (alpha > 0 && fading) {
            alpha -= 0.0018f;
            if(alpha <0){
                alpha = 0;
            }
        }
        if (!fading && timer.timeEvent(300)) {
            if(index  < text.length()) {
                index += 1;
            }
            else {
                fading = true;
            }
        }
        pushSpaceToStartVisible = ((int) (System.nanoTime() * 0.0000000075) % 3) > 0;


    }

    @Override
    public void draw(Graphics2D g2d) {

        g2d.drawImage(assets.getBackground("SPLASH_SCREEN"),0,0,1500,800,null);
        if(pushSpaceToStartVisible){
            g2d.setColor(Color.RED);
            g2d.setFont(assets.getFont("MAIN").deriveFont(25f));
            g2d.drawString("PUSH SPACE TO START",587,760);
        }

        if(alpha > 0) {
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
            g2d.setColor(Color.black);
            g2d.fillRect(0, 0, Constants.WIDTH, Constants.HEIGHT);
        }
        if(!fading) {
            g2d.setFont(assets.getFont("SPLASH").deriveFont(60f));
            g2d.setColor(Color.BLUE);
            g2d.drawString(text.substring(0, index), 100, 400);
        }
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));





    }

    @Override
    public void init(Canvas canvas) {
        canvas.addKeyListener(this);
        canvas.setCursor(blankCursor);
    }


    public boolean validatePlayers(){
        if(playersManager.getAllPlayersNames().contains(playersManager.getCurrentPlayer())){
            return true;
        }
        else {
            return false;
        }
    }
    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_SPACE && fading) {
            pressed = true;
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
