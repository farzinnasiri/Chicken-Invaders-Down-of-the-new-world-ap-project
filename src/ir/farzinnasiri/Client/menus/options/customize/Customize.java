package ir.farzinnasiri.Client.menus.options.customize;


import ir.farzinnasiri.Client.buttons.Action;
import ir.farzinnasiri.Client.buttons.BackButton;
import ir.farzinnasiri.Client.buttons.Button;
import ir.farzinnasiri.Client.input.MouseInput;
import ir.farzinnasiri.Client.stateMachine.StateMachine;
import ir.farzinnasiri.Client.stateMachine.SuperStateMachine;
import ir.farzinnasiri.Utils.PlayerProperties;
import ir.farzinnasiri.Client.systemManager.PlayersManager;
import ir.farzinnasiri.Utils.Assets;
import ir.farzinnasiri.Utils.Constants;

import java.awt.*;
import java.util.ArrayList;

public class Customize extends SuperStateMachine {
    Assets assets;
    private static Cursor knifeCursor;
    private ArrayList<Button> buttons;
    private MouseInput mouseInput;
    private SpaceshipCustomization spaceshipCustomization;
    private EngineCustomization engineCustomization;
    private Presenter presenter;
    private BackButton backButton;
    private PlayerProperties playerProperties;
    private PlayersManager playersManager;


    public Customize(StateMachine stateMachine) {
        super(stateMachine);

        assets = Assets.getInstance();

        knifeCursor = Toolkit.getDefaultToolkit().createCustomCursor(
                assets.getOtherImageAssets("KNIFE"), new Point(0, 0), "knife Cursor");

        playersManager = PlayersManager.getInstance();
        playerProperties = playersManager.getPlayerProperties(playersManager.getCurrentPlayer());

        presenter = new Presenter(playerProperties);

        mouseInput = new MouseInput();

        buttons = new ArrayList<>();

        setButton();




    }



    private void setButton() {
        buttons.add(new Button(assets.getButton("SPACESHIP_OUT"), assets.getButton("SPACESHIP_IN"),
                50,
                Constants.HEIGHT / 2 - Constants.BIG_BUTTON_HEIGHT, new Action() {
            @Override
            public void doAction() {
                spaceshipCustomization = new SpaceshipCustomization(playerProperties);
                engineCustomization = null;
            }
        }));

        buttons.add(new Button(assets.getButton("ENGINE_OUT"), assets.getButton("ENGINE_IN"),
                50,
                Constants.HEIGHT / 2 , new Action() {
            @Override
            public void doAction() {
                engineCustomization = new EngineCustomization(playerProperties);
                spaceshipCustomization = null;
            }
        }));
        backButton = new BackButton(new Action() {
            @Override
            public void doAction() {
                setState(StateMachine.OPTIONS);
                engineCustomization = null;
                spaceshipCustomization = null;
                playersManager.savePlayerProperties(playerProperties);

            }
        });
    }


    @Override
    public void update(double delta) {
        presenter.update();
        backButton.update();
        for(Button b:buttons){
            b.update();
        }

        if(spaceshipCustomization != null) {
            spaceshipCustomization.update();

        }
        else if(engineCustomization != null){
            engineCustomization.update();
        }
    }

    @Override
    public void draw(Graphics2D g2d) {
        g2d.drawImage(assets.getBackground(("MENU_BACKGROUND")),0,0,null);


        presenter.draw(g2d);

        backButton.draw(g2d);

        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,0.85f));

        for(Button b: buttons){
            b.draw(g2d);
        }

        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,1));

        if(spaceshipCustomization != null) {
                spaceshipCustomization.draw(g2d);

        }else if(engineCustomization != null){
            engineCustomization.draw(g2d);
        }


    }


    @Override
    public void init(Canvas canvas) {
        canvas.setCursor(knifeCursor);
        canvas.addMouseListener(mouseInput);
        canvas.addMouseMotionListener(mouseInput);

    }
}
