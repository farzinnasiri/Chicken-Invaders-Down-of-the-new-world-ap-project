package ir.farzinnasiri.Client.menus.game;

import ir.farzinnasiri.Client.ClientConnectionHandler;
import ir.farzinnasiri.Client.animations.BarAnimation;
import ir.farzinnasiri.Client.animations.Message;
import ir.farzinnasiri.Client.audio.AudioManager;
import ir.farzinnasiri.Client.buttons.Action;
import ir.farzinnasiri.Client.buttons.Button;
import ir.farzinnasiri.Client.game.Background;
import ir.farzinnasiri.Client.input.MouseInput;
import ir.farzinnasiri.Client.stateMachine.StateMachine;
import ir.farzinnasiri.Client.stateMachine.SuperStateMachine;
import ir.farzinnasiri.Client.views.DrawableObject;
import ir.farzinnasiri.Client.views.GameObjects;
import ir.farzinnasiri.Client.views.MyPlayer;
import ir.farzinnasiri.Client.views.PlayerView;
import ir.farzinnasiri.Utils.*;
import ir.farzinnasiri.Client.systemManager.PlayersManager;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Map;

public class GameScreen extends SuperStateMachine {
    private final Message textFinal;
    private PlayersManager playersManager;
    private PlayerProperties playerProperties;
    private Background backOne;
    private Background backTwo;
    private BarAnimation scoreLifeBar;
    private BarAnimation playerPropertiesBar;
    private Cursor cursor;
    private AudioManager audioManager;
    private Message deathMessage;
    private MyPlayer myPlayer;
    private ClientConnectionHandler clientConnectionHandler;
    private int id;
    private GameObjects gameObjects;
    private boolean watching;
    private Assets assets;
    private Button enterGameBtn;
    private ServerTextBox serverTextBox;
    private boolean openToClick;
    private MouseInput mouseInput;
    private Timer soundTimer;


    public GameScreen(StateMachine stateMachine, ClientConnectionHandler clientConnectionHandler) {
        super(stateMachine);
        getInstances();

        backOne = new Background();
        backTwo = new Background(0, backOne.getImageHeight());


        mouseInput = new MouseInput();

        serverTextBox = new ServerTextBox();

        soundTimer = new Timer();


        this.clientConnectionHandler = clientConnectionHandler;

        this.watching = clientConnectionHandler.isWatching();

        setViewerEnterBtn(clientConnectionHandler);

        id = clientConnectionHandler.getId();


         textFinal = new Message(new Vector2D(Constants.WIDTH / 2,
                Constants.HEIGHT / 2), false,
                "GoodBye!!!", Color.RED, true);

        scoreLifeBar = new BarAnimation(assets.getBar("SCORE_LIFE_BAR"),
                new Vector2D(-assets.getBar("SCORE_LIFE_BAR").getWidth(),
                        0), 0, 0, 0, 3);


        playerPropertiesBar = new BarAnimation(assets.getBar("PLAYER_STATE_BAR"),
                new Vector2D(-assets.getBar("PLAYER_STATE_BAR").getWidth(),
                        Constants.HEIGHT - assets.getBar("PLAYER_STATE_BAR").getHeight()), -4
                , Constants.HEIGHT - assets.getBar("PLAYER_STATE_BAR").getHeight(), 0, 3);

        playGameMusic();
        setCursor();


        if (!watching) {
            addMyPlayer();

        }


    }

    public void playGameMusic() {
        audioManager.playGameMusics();


    }

    private void setViewerEnterBtn(ClientConnectionHandler clientConnectionHandler) {
        openToClick = true;
        if (watching) {
            enterGameBtn = new Button(assets.getButton("ENTER_GAME_OUT"), assets.getButton("ENTER_GAME_IN"),
                    (Constants.WIDTH - Constants.SMALL_BUTTON_WIDTH) / 2,
                    Constants.HEIGHT - Constants.SMALL_BUTTON_HEIGHT - 10, new Action() {
                @Override
                public void doAction() {
                    if (openToClick) {
                        openToClick = false;
                        Thread thread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                synchronized (clientConnectionHandler) {
                                    clientConnectionHandler.requestGameEntering();
                                    try {
                                        clientConnectionHandler.wait();
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }

                                    if (clientConnectionHandler.isWatching()) {
                                        openToClick = true;

                                    } else {
                                        changeViewToGame();

                                    }

                                }

                            }
                        });
                        thread.start();
                    }
                }
            });
        }
    }

    private void setCursor() {
        if (!watching) {
            Image blank = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
            cursor = Toolkit.getDefaultToolkit().createCustomCursor(
                    blank, new Point(0, 0), "blank cursor");
        } else {

            cursor = Toolkit.getDefaultToolkit().createCustomCursor(
                    assets.getOtherImageAssets("KNIFE"), new Point(0, 0), "knife Cursor");
        }
    }

    private void getInstances() {
        gameObjects = GameObjects.getInstance();

        assets = Assets.getInstance();

        audioManager = AudioManager.getInstance();

        playersManager = PlayersManager.getInstance();

        playerProperties = playersManager.getPlayerProperties(playersManager.getCurrentPlayer());

    }

    private void addMyPlayer() {
        myPlayer = (MyPlayer) gameObjects.getPlayers().get(id);
        try {
            myPlayer.setConnection(clientConnectionHandler);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }


    }

    private void changeViewToGame() {
        watching = false;
        addMyPlayer();
        setCursor();
        getStateMachine().init();

    }

    @Override
    public void update(double delta) {
        if (gameObjects.isPlaying()) {
            if (watching) enterGameBtn.update();
            serverTextBox.update();
            if (!scoreLifeBar.isPositioned()) {
                scoreLifeBar.update();
            }
            if (!playerPropertiesBar.isPositioned()) {
                playerPropertiesBar.update();
            }
            clientConnectionHandler.getScreen();
            if (!watching && myPlayer != null) myPlayer.update(delta);
        } else if (!gameObjects.isPlaying()) {
            pause(clientConnectionHandler);

        }


    }

    @Override
    public void draw(Graphics2D g2d) {


        if (gameObjects.isPlaying()) {
            backOne.draw(g2d);
            backTwo.draw(g2d);

            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.85f));

            scoreLifeBar.draw(g2d);

            playerPropertiesBar.draw(g2d);
            if (!watching && myPlayer != null) {
                drawHeatGage(g2d);

            }


            if (!watching) {
                if (playerPropertiesBar.isPositioned() && scoreLifeBar.isPositioned()) drawProperties(g2d);
            }

            drawTextBox(g2d);

            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));

            if (!gameObjects.isFinished()) {
                synchronized (gameObjects.getPlayers().entrySet()) {
                    for (Map.Entry<Integer, PlayerView> entry : gameObjects.getPlayers().entrySet()) {
                        if(!entry.getValue().isLost()){
                            entry.getValue().draw(g2d);
                        }
                    }
                }
                synchronized (gameObjects.getWeapons()) {
                    for (DrawableObject weapon : gameObjects.getWeapons()) {
                        weapon.draw(g2d);
                    }
                }

                synchronized (gameObjects.getExplosions()) {
                    for (DrawableObject explosion : gameObjects.getExplosions()) {
                        explosion.draw(g2d);
                    }
                    gameObjects.removeExplosion();

                }

                synchronized (gameObjects.getEnemies()) {
                    for (DrawableObject enemy : gameObjects.getEnemies()) {
                        enemy.draw(g2d);
                    }
                }

                synchronized (gameObjects.getScreenExtraObjects()) {
                    for (DrawableObject screenExtraObject : gameObjects.getScreenExtraObjects()) {
                        screenExtraObject.draw(g2d);
                    }
                }
            if (watching) enterGameBtn.draw(g2d);
            }else {
                drawFinalMessage(g2d);
            }


            drawMessages(g2d);

        }


    }

    private void drawFinalMessage(Graphics2D g2d) {
        if(!textFinal.isDead()){
            textFinal.draw(g2d);
        }else {
            gameObjects.init();
            setState(StateMachine.MENU);
        }

    }

    private void drawMessages(Graphics2D g2d) {
        synchronized (gameObjects.getMessages()) {
            if (gameObjects.getMessages().size() > 0) {
                gameObjects.getMessages().get(0).draw(g2d);
                if (gameObjects.getMessages().get(0).isDead()) {
                    gameObjects.getMessages().remove(0);
                }

            }
        }

    }

    private void drawHeatGage(Graphics2D g2d) {
        if (myPlayer != null) {
            g2d.setColor(Color.WHITE);
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.85f));
            for (int i = 0; i < myPlayer.getHeat() / 10; i++) {
                int gap = myPlayer.getMxHeat() / 35;
                g2d.fillRect(180 + (2 * i) * gap, 10, gap, 25);
            }

            if (myPlayer.isOverheated()) {
                if (soundTimer.timeEvent(4000)) {
                    audioManager.playSound("OVERHEAT", true);

                }
                g2d.setColor(Color.RED);
                g2d.setFont(Assets.getInstance().getFont("OVERHEAT").deriveFont(25f));

                String overheatText = (((int) (System.nanoTime() * 0.0000000075) % 3) > 0) ? "STOP SHOOTING!" : "";
                g2d.drawString(overheatText, 180 + (myPlayer.getMxHeat()
                        - g2d.getFontMetrics().stringWidth(overheatText)) / 2, 30);
            }
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));
        }


    }

    private void drawProperties(Graphics2D g2d) {
        g2d.setColor(Color.WHITE);
        g2d.setFont(Assets.getInstance().getFont("MAIN").deriveFont(30f));
        g2d.drawString(String.valueOf(myPlayer.getLife()), 40, Constants.HEIGHT - 15);
        g2d.drawString(String.valueOf(myPlayer.getMissile()), 110, Constants.HEIGHT - 15);
        g2d.drawString(String.valueOf(myPlayer.getFireLevel()), 195, Constants.HEIGHT - 15);
        g2d.drawString(String.valueOf(myPlayer.getFood()), 290, Constants.HEIGHT - 15);
        g2d.drawString(String.valueOf(myPlayer.getScore()), 10, 30);


    }

    private void drawTextBox(Graphics2D g2d) {
        g2d.setColor(Color.GREEN);
        g2d.setFont(Assets.getInstance().getFont("MAIN").deriveFont(15f));

        FontMetrics fm = g2d.getFontMetrics();
        String message = serverTextBox.getLastMessage();
        g2d.drawString(message,
                Constants.WIDTH - fm.stringWidth(message) - 10,
                Constants.HEIGHT - fm.getHeight() - 10);


    }

    @Override
    public void init(Canvas canvas) {
        canvas.setCursor(cursor);
        if (watching) {
            canvas.addMouseListener(mouseInput);
            canvas.addMouseMotionListener(mouseInput);
        } else {
            canvas.addKeyListener(myPlayer);
            canvas.addMouseMotionListener(myPlayer);
            canvas.addMouseListener(myPlayer);
        }
        canvas.addKeyListener(new KeyHandler());

    }

    class ServerTextBox implements Runnable {
        private ArrayList<String> messages;
        private Timer timer;

        public ServerTextBox() {
            messages = new ArrayList<>();
            Thread thread = new Thread(this, "text box thread");
            thread.start();

            timer = new Timer();

        }

        public void update() {
            if (timer.timeEvent(5000) && messages.size() > 0 && gameObjects.isPlaying()) {
                messages.remove(0);


            } else if (!gameObjects.isPlaying()) {
                timer.resetTimer();
            }


        }

        public String getLastMessage() {
            if (messages.size() > 0) {
                return messages.get(0);

            } else {
                return "";
            }
        }

        private void addMessage(String message) {
            if (message != null) {
                messages.add(message);
                if (messages.size() == 1) {
                    timer.resetTimer();
                }

            }
        }


        @Override
        public void run() {
            while (true) {
                addMessage(clientConnectionHandler.getServerAnswer());

                try {
                    Thread.sleep(15);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }

        }
    }


    class KeyHandler implements KeyListener {

        @Override
        public void keyTyped(KeyEvent e) {

        }

        @Override
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();
            if (key == KeyEvent.VK_ESCAPE) {
                clientConnectionHandler.sendCommend("ESCAPE_PRESSED");
            }

        }

        @Override
        public void keyReleased(KeyEvent e) {

        }
    }
}
