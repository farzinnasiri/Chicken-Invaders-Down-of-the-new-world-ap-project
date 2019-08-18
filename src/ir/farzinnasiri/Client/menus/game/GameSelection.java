package ir.farzinnasiri.Client.menus.game;

import ir.farzinnasiri.Client.ClientConnectionHandler;
import ir.farzinnasiri.Client.buttons.Action;
import ir.farzinnasiri.Client.buttons.BackButton;
import ir.farzinnasiri.Client.buttons.Button;
import ir.farzinnasiri.Client.input.MouseInput;
import ir.farzinnasiri.Client.stateMachine.StateMachine;
import ir.farzinnasiri.Client.stateMachine.SuperStateMachine;
import ir.farzinnasiri.Client.systemManager.PlayersManager;
import ir.farzinnasiri.Server.ServerMain;
import ir.farzinnasiri.Utils.Assets;
import ir.farzinnasiri.Utils.Constants;
import ir.farzinnasiri.Utils.PlayerProperties;
import ir.farzinnasiri.Utils.Timer;

import java.awt.*;
import java.util.ArrayList;

public class GameSelection extends SuperStateMachine {
    private Assets assets;
    private Cursor knifeCursor;
    private ArrayList<Button> buttons;
    private MouseInput mouseInput;
    private BackButton backButton;
    private boolean loading;
    private int finalIndex;
    private String loadingText;
    private Timer timer;
    private boolean open;

    public GameSelection(StateMachine stateMachine) {
        super(stateMachine);

        assets = Assets.getInstance();
        loadingText = "Loading...";
        finalIndex = loadingText.length() - 4;
        timer = new Timer();

        buttons = new ArrayList<>();
        mouseInput = new MouseInput();

        open = true;

        knifeCursor = Toolkit.getDefaultToolkit().createCustomCursor(
                assets.getOtherImageAssets("KNIFE"), new Point(0, 0), "blank cursor");


        backButton = new BackButton(new Action() {
            @Override
            public void doAction() {
                setState(StateMachine.MENU);
            }
        });

        buttons.add(new Button(assets.getButton("NEW_GAME_OUT"),
                assets.getButton("NEW_GAME_IN"),
                (Constants.WIDTH - Constants.BIG_BUTTON_WIDTH) / 2,
                Constants.HEIGHT / 2 - Constants.BIG_BUTTON_HEIGHT * 3 / 2, new Action() {
            @Override
            public void doAction() {
                if (open) {
                    startSinglePlayerGame(true);
                    open = false;

                }

            }
        }));
        buttons.add(new Button(assets.getButton("CONTINUE_OUT"),
                assets.getButton("CONTINUE_IN"),
                (Constants.WIDTH - Constants.BIG_BUTTON_WIDTH) / 2,
                Constants.HEIGHT / 2 - Constants.BIG_BUTTON_HEIGHT / 2, new Action() {
            @Override
            public void doAction() {
                if (open) {
                    startSinglePlayerGame(false);
                    open = false;

                }
            }
        }));

        buttons.add(new Button(assets.getButton("MULTI_PLAYER_OUT"), assets.getButton("MULTI_PLAYER_IN"),
                (Constants.WIDTH - Constants.BIG_BUTTON_WIDTH) / 2,
                Constants.HEIGHT / 2 + Constants.BIG_BUTTON_HEIGHT / 2 + 10, new Action() {
            @Override
            public void doAction() {
                setState(StateMachine.MULTI_PLAYER);
            }
        }));

        buttons.add(new Button(assets.getButton("JOIN_VIEWER_OUT"), assets.getButton("JOIN_VIEWER_IN"),
                (Constants.WIDTH - Constants.BIG_BUTTON_WIDTH) / 2,
                Constants.HEIGHT / 2 + Constants.BIG_BUTTON_HEIGHT * 2 + 10, new Action() {
            @Override
            public void doAction() {
                setState(StateMachine.VIEW_SCREEN);
            }
        }));


    }

    private void startSinglePlayerGame(boolean newGame) {
        int waveToStart = 1;
        if (!newGame) {
            PlayerProperties playerProperties = PlayersManager.getInstance()
                    .getPlayerProperties(PlayersManager.getInstance().getCurrentPlayer());
            if (playerProperties.getLife() == 0) {
                return;
            } else {
                waveToStart = playerProperties.getWavesPassed().get((playerProperties.getWavesPassed().size() - 1)) + 1;
                System.out.println(waveToStart);
            }
        } else {
            waveToStart = 1;
            PlayersManager.getInstance().newGame();

        }
        ClientConnectionHandler connection = new ClientConnectionHandler(false, getStateMachine());
        ServerMain.main(new String[]{"8888", "1", String.valueOf(waveToStart), "20"});
        connection.init("127.0.0.1", Integer.valueOf("8888"));
        connection.start();
        loading = true;
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Timer timer = new Timer();
                while (true) {
                    if (connection.isConnected() && timer.timeEvent(2000)) {
                        loading = false;
                        connection.sendCommend("MASTER");
                        connection.sendCommend("SINGLEPLAYER");
                        startGameScreen(connection);
                        break;

                    }
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
        });
        thread.start();


    }

    @Override
    public void update(double delta) {
        backButton.update();
        for (Button b : buttons) {
            b.update();
        }
        if (timer.timeEvent(200)) {
            if (finalIndex == loadingText.length()) {
                finalIndex = loadingText.length() - 4;
            }
            finalIndex += 1;
        }

    }

    @Override
    public void draw(Graphics2D g2d) {
        g2d.drawImage(assets.getBackground(("MENU_BACKGROUND")), 0, 0, null);
        backButton.draw(g2d);

        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.85f));

        if (loading) {

            g2d.setFont(Assets.getInstance().getFont("MAIN").deriveFont(40f));
            g2d.setColor(Color.white);

            FontMetrics fm = g2d.getFontMetrics();

            g2d.drawString(loadingText.substring(0, finalIndex),
                    (Constants.WIDTH - fm.stringWidth(loadingText)) / 2, 200);


        }

        for (Button b : buttons) {
            b.draw(g2d);
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
