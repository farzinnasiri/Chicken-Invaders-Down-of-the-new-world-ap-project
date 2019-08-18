package ir.farzinnasiri.Client.menus;


import ir.farzinnasiri.Client.audio.AudioManager;
import ir.farzinnasiri.Client.input.Keyboard;
import ir.farzinnasiri.Client.input.MouseInput;
import ir.farzinnasiri.Client.stateMachine.StateMachine;
import ir.farzinnasiri.Client.stateMachine.SuperStateMachine;
import ir.farzinnasiri.Utils.Assets;
import ir.farzinnasiri.Utils.Constants;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Credits extends SuperStateMachine {

    private BufferedImage textImage;
    private double textY;
    private Keyboard keyboard;
    private AudioManager audioManager;

    public Credits(StateMachine stateMachine) {
        super(stateMachine);
        textImage = Assets.getInstance().getBackground("CREDITS_TEXT");
        keyboard = new Keyboard();

        audioManager = AudioManager.getInstance();

        audioManager.playCreditsMusic();

    }

    @Override
    public void update(double delta) {
        textY += 0.5;
        if(keyboard.pressed()!= -1 || textY >= 1700){
            keyboard.resetKeyboard();
            textY = 0;
            audioManager.stopCreditsMusic();
            audioManager.playMainTheme();
            setState(StateMachine.MENU);
        }

    }

    @Override
    public void draw(Graphics2D g2d) {
        g2d.drawImage(Assets.getInstance().getBackground("MENU_BACKGROUND"),0,0,null);
        draw3DScrollingText(g2d);

    }



    private void draw3DScrollingText(Graphics2D g2d) {
        double horizontalNarrowing = 0;

            for (int screenY = 0; screenY < 750; screenY++) {
                int textureY = (int) (Math.tan(1.3 * (screenY / 800.0)) * -150 + textY);
                int dx1 = (int) (horizontalNarrowing - 100);
                int dx2 = (int) (Constants.WIDTH + 100 - horizontalNarrowing);
                g2d.drawImage(textImage, dx1, 800 - screenY, dx2, 799 - screenY
                        , 0, textureY, textImage.getWidth(), textureY + 1, null);
                horizontalNarrowing += 0.675;

        }
    }

    @Override
    public void init(Canvas canvas) {
        canvas.addKeyListener(keyboard);



    }
}
