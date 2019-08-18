package ir.farzinnasiri.Client.menus.players;



import ir.farzinnasiri.Client.buttons.Action;
import ir.farzinnasiri.Client.buttons.StringButton;
import ir.farzinnasiri.Client.input.MouseInput;
import ir.farzinnasiri.Client.stateMachine.StateMachine;
import ir.farzinnasiri.Client.stateMachine.SuperStateMachine;
import ir.farzinnasiri.Client.systemManager.PlayersManager;
import ir.farzinnasiri.Client.buttons.Button;
import ir.farzinnasiri.Utils.Assets;
import ir.farzinnasiri.Utils.Constants;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class PlayersState extends SuperStateMachine {
    private Assets assets;
    private  Cursor knifeCursor;
    private MouseInput mouseInput;
    private PlayersManager playersManager;
    private ArrayList<String> playersNames;
    private ArrayList<Button> controlButtons;
    private ArrayList<Button> stringButtons;

    private AddPlayer addPlayer;


    private BufferedImage playerBox;

    private BufferedImage addBtnIn;
    private BufferedImage addBtnOut;
    private BufferedImage deleteBtnIn;
    private BufferedImage deleteBtnOut;
    private BufferedImage doneBtnIn;
    private BufferedImage doneBtnOut;


    private int margine;


    public PlayersState(StateMachine stateMachine) {
        super(stateMachine);
        mouseInput = new MouseInput();
        assets = Assets.getInstance();

        controlButtons = new ArrayList<>();
        stringButtons = new ArrayList<>();

        playersManager = PlayersManager.getInstance();
        playersNames = playersManager.getAllPlayersNames();

        playerBox = assets.getBox("PLAYERS");
        addPlayer = new AddPlayer(playersManager);

        this.addBtnIn = assets.getButton("ADD_IN");
        this.addBtnOut = assets.getButton("ADD_OUT");
        this.deleteBtnIn = assets.getButton("DELETE_IN");
        this.deleteBtnOut = assets.getButton("DELETE_OUT");
        this.doneBtnIn = assets.getButton("DONE_IN");
        this.doneBtnOut = assets.getButton("DONE_OUT");

        margine = (Constants.WIDTH - 3 * Constants.SMALL_BUTTON_WIDTH) / 4;
        setControlButtons();
        setNameButtons();

        knifeCursor = Toolkit.getDefaultToolkit().createCustomCursor(
                assets.getOtherImageAssets("KNIFE"), new Point(0, 0), "blank cursor");



    }


    private void setControlButtons() {
        //add

        controlButtons.add(new Button(addBtnOut, addBtnIn, margine,
                Constants.HEIGHT - Constants.SMALL_BUTTON_HEIGHT - 15, new Action() {
            @Override
            public void doAction() {
                addPlayer.setAdding(true);
            }
        }));
        //delete
        controlButtons.add(new Button(deleteBtnOut, deleteBtnIn, margine * 2 + Constants.SMALL_BUTTON_WIDTH,
                Constants.HEIGHT - Constants.SMALL_BUTTON_HEIGHT - 15, new Action() {
            @Override
            public void doAction() {
                if(playersNames.size()-1 > 0) {
                    playersManager.deletePlayer(playersManager.getCurrentPlayer());
                    playersNames = playersManager.getAllPlayersNames();
                    setNameButtons();
                    playersManager.setCurrentPlayer(playersNames.get(0));
                }

            }
        }));
        //done
        controlButtons.add(new Button(doneBtnOut, doneBtnIn, margine * 3 + Constants.SMALL_BUTTON_WIDTH * 2,
                Constants.HEIGHT - Constants.SMALL_BUTTON_HEIGHT - 15, new Action() {
            @Override
            public void doAction() {
                if(playersManager.getAllPlayersNames().size() > 0) {
                    playersManager.saveCurrentPlayer();
                    setState(StateMachine.MENU);
                }

            }
        }));


    }

    private void setNameButtons() {
        stringButtons.clear();
        int i = 1;
        for (String name : playersNames) {
            stringButtons.add(new StringButton(name, (Constants.WIDTH - playerBox.getWidth()) / 2 + 400,
                    250 + i * 45, new Action() {
                @Override
                public void doAction() {
                    playersManager.setCurrentPlayer(name);

                }
            }
            ));
            i++;


        }
    }

    @Override
    public void update(double delta) {

        if(playersManager.getAllPlayersNames().size()-playersNames.size() != 0){
            playersNames = playersManager.getAllPlayersNames();
            setNameButtons();
        }
        if (addPlayer.isAdding()) {
            addPlayer.update();

        } else {
            for (Button controlButton : controlButtons) {
                controlButton.update();
            }
            for (Button stringButton : stringButtons) {
                stringButton.update();
            }
        }

    }

    @Override
    public void draw(Graphics2D g2d) {

        g2d.drawImage(assets.getBackground(("MENU_BACKGROUND")), 0, 0, null);
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.80f));

        g2d.drawImage(playerBox, (Constants.WIDTH - playerBox.getWidth()) / 2,
                (Constants.HEIGHT - playerBox.getHeight()) / 2, null);

        g2d.setFont(assets.getFont("MAIN").deriveFont(24f));
        g2d.setColor(Color.YELLOW);
        FontMetrics fm = g2d.getFontMetrics();
        String log;
        if(playersNames.size() == 0){
            log = "Please add a player";

        }else{
        log = playersManager.getCurrentPlayer() + " is selected";
        }
        g2d.drawString(log, (Constants.WIDTH - fm.stringWidth(log)) / 2, 250);

        for (Button controlButton : controlButtons) {
            controlButton.draw(g2d);
        }
        for (Button stringButton : stringButtons) {
            stringButton.draw(g2d);
        }
        if (addPlayer.isAdding()) {

            addPlayer.draw(g2d);

        }


        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
    }

    @Override
    public void init(Canvas canvas) {
        canvas.setCursor(knifeCursor);
        canvas.addMouseListener(mouseInput);
        canvas.addMouseMotionListener(mouseInput);
        canvas.addKeyListener(addPlayer);

    }


}
