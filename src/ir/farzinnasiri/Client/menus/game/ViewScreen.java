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
import ir.farzinnasiri.Utils.Timer;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

public class ViewScreen extends SuperStateMachine implements KeyListener {
    private Assets assets;
    private Cursor knifeCursor;
    private ArrayList<Button> buttons;
    private MouseInput mouseInput;
    private BackButton backButton;
    private String portInput;
    private String port;
    private boolean establishing;
    private boolean connecting;
    private String bar;
    private String message;
    private final ClientConnectionHandler connection;
    private boolean open;
    private Timer timer;
    private Timer gameTimer;
    private boolean error;
    private int finalIndex;
    private String loadingText;


    public ViewScreen(StateMachine stateMachine) {
        super(stateMachine);

        connection = new ClientConnectionHandler(true,getStateMachine());

        connecting = true;

        timer = new Timer();
        gameTimer = new Timer();

        loadingText = "Wait a sec...!";
        finalIndex = loadingText.length()-4;

        portInput ="Please type a valid port and hit enter:" ;

        open = true;

        assets = Assets.getInstance();
        knifeCursor = Toolkit.getDefaultToolkit().createCustomCursor(
                assets.getOtherImageAssets("KNIFE"), new Point(0, 0), "knife cursor");

        port = "";
        message = "";
        bar = "";
        mouseInput = new MouseInput();

        backButton = new BackButton(new Action() {
            @Override
            public void doAction() {
                setState(StateMachine.GAME_SELECTION);
            }
        });

    }


    @Override
    public void update(double delta) {
        backButton.update();


        if (connecting) {
            if (((int) (System.nanoTime() * 0.0000000075) % 3) > 0) {
                bar = "_";
            } else {
                bar = "";
            }
        }
        synchronized (connection) {
            if (!open) {
                if (timer.timeEvent(200)) {
                    if (finalIndex == loadingText.length()) {
                        finalIndex = loadingText.length() - 4;
                    }
                    finalIndex += 1;
                }
                if (gameTimer.timeEvent(3000)) {
                    if (connection.isConnected()) {
                        startGameScreen(connection);


                    } else {
                        message = connection.getError();
                        error = true;
                        connection.stop();
                        open = true;
                    }
                }
            }
        }

    }

    @Override
    public void draw(Graphics2D g2d) {
        g2d.drawImage(assets.getBackground(("MENU_BACKGROUND")), 0, 0, null);
        backButton.draw(g2d);

        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.85f));

        if (connecting) {
            drawPortInput(g2d);
        }
        if (!open & !error) {
                g2d.setFont(Assets.getInstance().getFont("MAIN").deriveFont(40f));
                g2d.setColor(Color.white);

                FontMetrics fm = g2d.getFontMetrics();

                g2d.drawString(loadingText.substring(0, finalIndex),
                        (Constants.WIDTH - fm.stringWidth(loadingText)) / 2, Constants.HEIGHT/2 -100);


        }


        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));

    }

    private void drawPortInput(Graphics2D g2d) {

        g2d.setColor(Color.WHITE);
        g2d.setFont(assets.getFont("MAIN").deriveFont(30f));
        FontMetrics fm = g2d.getFontMetrics();
        g2d.drawString(portInput, (Constants.WIDTH - fm.stringWidth(portInput)) / 2,
                (Constants.HEIGHT - fm.getHeight()) / 2);

        g2d.drawString(port + bar, (Constants.WIDTH - fm.stringWidth(port)) / 2,
                (Constants.HEIGHT - fm.getHeight()) / 2 + 50);

        g2d.setColor(Color.RED);
        g2d.drawString(message,
                (Constants.WIDTH - fm.stringWidth(message)) / 2,
                (Constants.HEIGHT - fm.getHeight()) / 2 + 100);


    }


    @Override
    public void init(Canvas canvas) {

        canvas.setCursor(knifeCursor);
        canvas.addMouseListener(mouseInput);
        canvas.addMouseMotionListener(mouseInput);
        canvas.addKeyListener(this);

    }

    @Override
    public void keyTyped(KeyEvent e) {
        if (establishing || connecting) {
            if (e.getKeyChar() >= 48 && e.getKeyChar() <= 57) {
                if (!(port.length() > 4)) {
                    port += e.getKeyChar();
                }
            }
        }

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (open) {
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                if (portValid(port)) {

                    synchronized (connection) {
                        gameTimer.resetTimer();
                        connection.init("127.0.0.1", Integer.valueOf(port));
                        connection.start();
                        open = false;
                    }
                } else {
                    message = "sorry! try again";
                }
            } else if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
                open =true;
                if (port.length() > 0) {
                    port = port.substring(0, port.length() - 1);
                }
            }
        }


    }


    private boolean portValid(String port) {
        String regex = "\\d+";

        if (port.matches(regex)) {
            int portNumber = Integer.valueOf(port);
            return portNumber <= 65535 && portNumber >= 1000;


        }
        return false;
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
