package ir.farzinnasiri.Client.menus.options;


import ir.farzinnasiri.Client.buttons.Action;
import ir.farzinnasiri.Client.buttons.BackButton;
import ir.farzinnasiri.Client.buttons.Button;
import ir.farzinnasiri.Client.input.MouseInput;
import ir.farzinnasiri.Client.stateMachine.StateMachine;
import ir.farzinnasiri.Client.stateMachine.SuperStateMachine;
import ir.farzinnasiri.Utils.Assets;
import ir.farzinnasiri.Utils.Constants;

import java.awt.*;
import java.util.ArrayList;

public class Options extends SuperStateMachine {
    private Assets assets;
    private Cursor knifeCursor;
    private ArrayList<Button> buttons;
    private MouseInput mouseInput;
    private BackButton backButton;

    public Options(StateMachine stateMachine) {
        super(stateMachine);


        assets = Assets.getInstance();

        backButton = new BackButton(new Action() {
            @Override
            public void doAction() {
                setState(StateMachine.MENU);
            }
        });

        mouseInput = new MouseInput();

        buttons = new ArrayList<Button>();

        knifeCursor = Toolkit.getDefaultToolkit().createCustomCursor(
                assets.getOtherImageAssets("KNIFE"), new Point(0, 0), "blank cursor");

        buttons.add(new Button(assets.getButton("CUSTOMIZE_OUT"), assets.getButton("CUSTOMIZE_IN"),
                (Constants.WIDTH - Constants.BIG_BUTTON_WIDTH) / 2,
                Constants.HEIGHT / 2 - Constants.BIG_BUTTON_HEIGHT, new Action() {
            @Override
            public void doAction() {
                setState(StateMachine.CUSTOMIZE);
            }
        }));

        buttons.add(new Button(assets.getButton("SOUND_OUT"),
                assets.getButton("SOUND_IN"),
                (Constants.WIDTH - Constants.BIG_BUTTON_WIDTH) / 2,
                Constants.HEIGHT / 2 , new Action() {
            @Override
            public void doAction() {
                setState(StateMachine.SOUND_SETTINGS);
            }
        }));

    }



    @Override
    public void update(double delta) {
        backButton.update();
        for(Button b:buttons){
            b.update();
        }

    }

    @Override
    public void draw(Graphics2D g2d) {
        g2d.drawImage(assets.getBackground(("MENU_BACKGROUND")),0,0,null);
        backButton.draw(g2d);

        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,0.85f));
        for(Button b: buttons){
            b.draw(g2d);
        }
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,1));

    }


    @Override
    public void init(Canvas canvas) {
        canvas.setCursor(knifeCursor);
        canvas.addMouseListener(mouseInput);
        canvas.addMouseMotionListener(mouseInput);

    }
}
