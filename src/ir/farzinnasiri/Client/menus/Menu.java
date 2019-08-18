package ir.farzinnasiri.Client.menus;

import ir.farzinnasiri.Client.audio.AudioManager;
import ir.farzinnasiri.Client.buttons.Button;
import ir.farzinnasiri.Client.input.MouseInput;
import ir.farzinnasiri.Client.stateMachine.StateMachine;
import ir.farzinnasiri.Client.stateMachine.SuperStateMachine;
import ir.farzinnasiri.Client.systemManager.PlayersManager;
import ir.farzinnasiri.Utils.Assets;
import ir.farzinnasiri.Utils.Constants;
import ir.farzinnasiri.Client.buttons.*;



import java.awt.*;
import java.util.ArrayList;

public class Menu extends SuperStateMachine {
    private ArrayList<Button> buttons;
    private MouseInput mouseInput;
    private Cursor knifeCursor;
    private PlayersManager playersManager;
    private AudioManager audioManager;
    private Assets assets;



    public Menu(StateMachine stateMachine) {
        super(stateMachine);
        buttons = new ArrayList<>();
        mouseInput = new MouseInput();


        audioManager = AudioManager.getInstance();

        playersManager = PlayersManager.getInstance();

        assets = Assets.getInstance();

        knifeCursor = Toolkit.getDefaultToolkit().createCustomCursor(
                assets.getOtherImageAssets("KNIFE"), new Point(0, 0), "knife cursor");

        int hMargine = assets.getButton("SAVE_THE_WORLD_OUT").getWidth()/2;
        int vMargine = assets.getButton("SAVE_THE_WORLD_OUT").getHeight();
        audioManager.stopGameMusics();
        audioManager.playMainTheme();

        //Play Button
        buttons.add(new Button(
                assets.getButton("SAVE_THE_WORLD_OUT"),
                assets.getButton("SAVE_THE_WORLD_IN"),
               Constants.WIDTH/2 -hMargine,
                430,
                new Action() {
                    @Override
                    public void doAction() {
                        setState(StateMachine.GAME_SELECTION);

                    }
                }
        ));

        //Hall of Fame Button
        buttons.add(new Button(
                assets.getButton("HALL_OF_FAME_OUT"),
                assets.getButton("HALL_OF_FAME_IN"),
                (Constants.WIDTH/2 -hMargine),
                430+vMargine,
                new Action() {
                    @Override
                    public void doAction() {
                        setState(StateMachine.HALL_OF_FAME);

                    }
                }
        ));
        //Options Button
        buttons.add(new Button(
                assets.getButton("OPTIONS_OUT"),
                assets.getButton("OPTIONS_IN"),
                (Constants.WIDTH/2 -hMargine),
                430 + 2*vMargine,
                new Action() {
                    @Override
                    public void doAction() {
                        setState(StateMachine.OPTIONS);
                    }
                }
        ));
        //Credits Button
        buttons.add(new Button(
                assets.getButton("CREDITS_OUT"),
                assets.getButton("CREDITS_IN"),
                (Constants.WIDTH/2 -hMargine),
                430 + 3*vMargine,
                new Action() {
                    @Override
                    public void doAction() {
                        audioManager.stopMainTheme();
                        setState(StateMachine.CREDITS);
                    }
                }
        ));

        //Exit Button
        buttons.add(new Button(
                assets.getButton("QUIT_OUT"),
                assets.getButton("QUIT_IN"),
                50,
                440 + 3*vMargine,
                new Action() {
                    @Override
                    public void doAction() {
                        System.exit(0);

                    }
                }
        ));

        //Players Button
        buttons.add(new StringButton("Hey "+playersManager.getCurrentPlayer()+"!",
                Constants.WIDTH/2, 380, new Action() {
            @Override
            public void doAction() {
                setState(StateMachine.PLAYERS);

            }
        }));
    }





    @Override
    public void update(double delta) {
        for(Button b: buttons) {
            b.update();
        }

    }

    @Override
    public void draw(Graphics2D g2d) {
        g2d.drawImage(assets.getBackground("MENU_BACKGROUND"),0,0,null);

        g2d.drawImage(assets.getBackground("LOGO"),
                (Constants.WIDTH- assets.getBackground("LOGO").getWidth())/2,10,null);
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.85f));

        for(Button b: buttons) {
            b.draw(g2d);
        }

        if(!PlayersManager.getInstance().isDataBaseConnected()){
            g2d.setColor(Color.RED);
            g2d.drawString("Data Base Not working!",100,400);
        }

        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));








    }

    @Override
    public void init(Canvas canvas) {
        canvas.setCursor(knifeCursor);
        canvas.addMouseListener(mouseInput);
        canvas.addMouseMotionListener(mouseInput);

    }
}
