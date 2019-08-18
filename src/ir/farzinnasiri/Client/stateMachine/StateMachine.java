package ir.farzinnasiri.Client.stateMachine;


import ir.farzinnasiri.Client.ClientConnectionHandler;
import ir.farzinnasiri.Client.menus.Credits;
import ir.farzinnasiri.Client.menus.Menu;
import ir.farzinnasiri.Client.menus.PauseMenu;
import ir.farzinnasiri.Client.menus.game.GameScreen;
import ir.farzinnasiri.Client.menus.game.GameSelection;
import ir.farzinnasiri.Client.menus.game.MultiPlayer;
import ir.farzinnasiri.Client.menus.game.ViewScreen;
import ir.farzinnasiri.Client.menus.hallOfFame.HallOfFame;
import ir.farzinnasiri.Client.menus.options.Options;
import ir.farzinnasiri.Client.menus.options.SoundSettings;
import ir.farzinnasiri.Client.menus.options.customize.Customize;
import ir.farzinnasiri.Client.menus.players.PlayersState;
import ir.farzinnasiri.Client.menus.splashScreen.SplashScreen;

import java.awt.*;

public class StateMachine {

    public static final int SPLASH_SCREEN = -1;
    public static final int MENU = 0;
    public static final int GAME_SCREEN = 1;
    public static final int PLAYERS = 3;
    public static final int PAUSE_MENU = 2;
    public static final int OPTIONS = 4;
    public static final int CUSTOMIZE = 5;
    public static final int SOUND_SETTINGS = 6;
    public static final int CREDITS = 7;
    public static final int HALL_OF_FAME = 8;
    public static final int MULTI_PLAYER = 9;
    public static final int GAME_SELECTION = 10;
    public static final int VIEW_SCREEN = 11;

    private Canvas canvas;
    private byte selectState = 0;
    private SuperStateMachine gameState;
    private int state;
    private boolean resume;
    private GameScreen gameScreen;

    public StateMachine(Canvas canvas) {
        this.canvas = canvas;


        state = SPLASH_SCREEN;
        loadState(state);


    }

    public void draw(Graphics2D g2d) {
        if (gameState != null) {
            gameState.draw(g2d);
        }


    }

    public void update(double elapsed) {
        if (gameState != null) {
            gameState.update(elapsed);
        }

    }

    public void setState(int state) {
        init();

        this.state = state;
        loadState(this.state);

    }


    public void startGameScreen(ClientConnectionHandler clientConnectionHandler) {
        gameScreen = new GameScreen(this, clientConnectionHandler);
        gameState = gameScreen;
        init();


    }

    private void loadState(int state) {
        switch (state) {
            case -1:
                gameState = new SplashScreen(this);

                break;
            case 0:
                gameState = new Menu(this);
                break;
            case 1:
                gameState = gameScreen;
                gameScreen.playGameMusic();
                break;

            case 3:
                gameState = new PlayersState(this);
                break;
            case 4:
                gameState = new Options(this);
                break;
            case 5:
                gameState = new Customize(this);
                break;
            case 6:
                gameState = new SoundSettings(this);
                break;
            case 7:
                gameState = new Credits(this);
                break;
            case 8:
                gameState = new HallOfFame(this);
                break;
            case 9:
                gameState = new MultiPlayer(this);
                break;
            case 10:
                gameState = new GameSelection(this);
                break;
            case 11:
                gameState = new ViewScreen(this);
                break;


        }
        init();

    }

    public void init() {
        for (int j = 0; j < canvas.getKeyListeners().length; j++) {
            canvas.removeKeyListener(canvas.getKeyListeners()[j]);


        }
        for (int j = 0; j < canvas.getMouseListeners().length; j++) {
            canvas.removeMouseListener(canvas.getMouseListeners()[j]);
        }

        for (int j = 0; j < canvas.getMouseMotionListeners().length; j++) {
            canvas.removeMouseMotionListener(canvas.getMouseMotionListeners()[j]);
        }


        gameState.init(canvas);


    }

    public byte getSelectState() {
        return selectState;
    }

    public void pause(ClientConnectionHandler clientConnectionHandler) {
        gameState = new PauseMenu(this,clientConnectionHandler);
        init();
    }
}
