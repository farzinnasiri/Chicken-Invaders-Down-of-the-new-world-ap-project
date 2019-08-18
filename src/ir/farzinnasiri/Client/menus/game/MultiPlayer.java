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

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

public class MultiPlayer extends SuperStateMachine implements KeyListener {
    private String capacityInput;
    private Assets assets;
    private Cursor knifeCursor;
    private ArrayList<Button> buttons;
    private MouseInput mouseInput;
    private BackButton backButton;
    private String portInput;
    private boolean establishing;
    private boolean connecting;
    private String bar;
    private String inputField;
    private String message;
    private ClientConnectionHandler connection;
    private boolean open;
    private Timer timer;
    private double angle;
    private String maxCapacity;
    private boolean gettingPort;
    private boolean gettingCapacity;
    private String port;
    private boolean gettingWaves;
    private String maxWaves;


    public MultiPlayer(StateMachine stateMachine) {
        super(stateMachine);
        connection = new ClientConnectionHandler(false, getStateMachine());

        maxCapacity = "2";
        message = "";

        timer = new Timer();

        open = true;

        assets = Assets.getInstance();
        knifeCursor = Toolkit.getDefaultToolkit().createCustomCursor(
                assets.getOtherImageAssets("KNIFE"), new Point(0, 0), "knife cursor");

        inputField = "";
        bar = "";
        portInput = "Please Enter a valid port number and hit enter(port 8888 has already been reserved):";
        capacityInput = "Please Enter maximum players (default is 2) and hit enter:";

        buttons = new ArrayList<>();
        mouseInput = new MouseInput();

        backButton = new BackButton(new Action() {
            @Override
            public void doAction() {
                setState(StateMachine.GAME_SELECTION);
            }
        });
        buttons.add(new Button(
                assets.getButton("ESTABLISH_SERVER_OUT"),
                assets.getButton("ESTABLISH_SERVER_IN"),
                (Constants.WIDTH - 2 * Constants.SMALL_BUTTON_WIDTH) / 3,
                200,
                new Action() {
                    @Override
                    public void doAction() {
                        inputField = "";
                        gettingPort = true;
                        gettingCapacity = false;
                        establishing = true;
                        connecting = false;


                    }
                }
        ));

        buttons.add(new Button(
                assets.getButton("CONNECT_SERVER_OUT"),
                assets.getButton("CONNECT_SERVER_IN"),
                Constants.SMALL_BUTTON_WIDTH + 2 * (Constants.WIDTH - 2 * Constants.SMALL_BUTTON_WIDTH) / 3,
                200,
                new Action() {
                    @Override
                    public void doAction() {
                        inputField = "";
                        gettingPort = true;
                        connecting = true;
                        establishing = false;

                    }
                }
        ));
    }


    @Override
    public void update(double delta) {
        backButton.update();
        for (Button button : buttons) {
            button.update();
        }

        if (establishing || connecting) {
            if (((int) (System.nanoTime() * 0.0000000075) % 3) > 0) {
                bar = "_";
            } else {
                bar = "";
            }
        }
        synchronized (connection) {
            if (!open) {
                angle += delta * (2 * Math.PI / 3);
                if (timer.timeEvent(5000)) {
                    angle = 0;
                    System.out.println(connection.isConnected());
                    if (connection.isConnected()) {
                        connection.sendCommend("MULTIPLAYER");
                        System.out.println("starting");
                        startGameScreen(connection);
                        timer.resetTimer();

                    } else {
                        message = connection.getError();
                        connection.stop();
                        connection = new ClientConnectionHandler(false, getStateMachine());

                        gettingPort = true;
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

        for (Button b : buttons) {
            b.draw(g2d);
        }

        if (establishing || connecting) {
            drawUserInput(g2d);
        }
        if (!open) {
            if (((int) (System.nanoTime() * 0.0000000075) % 3) > 0) {
                FontMetrics fm = g2d.getFontMetrics();
                String waitingMessage = "Be Patient";
                g2d.setColor(Color.YELLOW);
                g2d.drawString(waitingMessage, (Constants.WIDTH - fm.stringWidth(waitingMessage)) / 2, 250);
            }
            g2d.setColor(Color.WHITE);
            g2d.fillArc(Constants.WIDTH / 2 - 35, 700, 70, 70, 90, -(int) angle);
        }


        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));

    }

    private void drawUserInput(Graphics2D g2d) {

        g2d.setColor(Color.WHITE);
        g2d.setFont(assets.getFont("MAIN").deriveFont(25f));
        FontMetrics fm = g2d.getFontMetrics();
        if (gettingPort) {
            g2d.drawString(portInput, (Constants.WIDTH - fm.stringWidth(portInput)) / 2,
                    (Constants.HEIGHT - fm.getHeight()) / 2);
        } else if (gettingCapacity) {
            g2d.drawString(capacityInput, (Constants.WIDTH - fm.stringWidth(capacityInput)) / 2,
                    (Constants.HEIGHT - fm.getHeight()) / 2);
        } else if (gettingWaves) {
            String waveInput = "How many waves do you want to play?(max:20)";
            g2d.drawString(waveInput, (Constants.WIDTH - fm.stringWidth(waveInput)) / 2,
                    (Constants.HEIGHT - fm.getHeight()) / 2);
        }

        g2d.drawString(inputField + bar, (Constants.WIDTH - fm.stringWidth(inputField)) / 2,
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
                inputField += e.getKeyChar();

            }
        }
    }


    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER && open) {
            if (gettingPort) {
                if (portValid(inputField) && establishing) {
                    port = inputField;
                    inputField = "";
                    gettingCapacity = true;
                    gettingPort = false;
                } else if (portValid(inputField) && connecting) {
                    port = inputField;
                    gettingPort = false;
                    open = false;
                    PlayersManager.getInstance().newGame();
                    connect();

                } else if (!portValid(inputField)) {
                    message = "sorry! port is not valid";
                    inputField = "";
                }

            } else if (gettingCapacity) {
                if (capacityValid(inputField)) {
                    gettingWaves = true;
                    gettingCapacity = false;
                    if (!inputField.equals("")) {
                        maxCapacity = inputField;
                    }
                    inputField = "";


                } else {
                    message = "please enter a number!";
                    inputField = "";
                }


            } else if (gettingWaves) {
                if (capacityValid(inputField)) {
                    if (!inputField.equals("")) {
                        maxWaves = inputField;
                        gettingWaves = false;
                        open = false;
                        ServerMain.main(new String[]{port, maxCapacity, "1", maxWaves});
                        connect();
                    } else {
                        inputField = "";
                        message = "please enter a number!";

                    }
                } else {
                    inputField = "";
                    message = "please enter a number!";
                }


            }
        } else if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
            open = true;
            message = "";
            if (inputField.length() > 0) {
                inputField = inputField.substring(0, inputField.length() - 1);
            }
        }
    }

    private boolean capacityValid(String maxCapacity) {

        String regex = "\\d+";
        System.out.println(maxCapacity.matches(regex));

        return (maxCapacity.matches(regex) || maxCapacity.equals(""));
    }

    private void connect() {
        synchronized (connection) {
            timer.resetTimer();
            connection.init("127.0.0.1", Integer.valueOf(port));
            connection.start();
        }

    }

    private boolean portValid(String port) {
        if (port.equals("8888")) {
            message = "this port is unavailable";
            return false;
        }
        if (port.equals("0")) {
            message = "server can not be empty!";
            return false;
        }
        String regex = "\\d+";

        if (port.matches(regex)) {
            int portNumber = Integer.valueOf(port);
            System.out.println("heyy");
            return portNumber <= 65535 && portNumber >= 1000;


        }
        return false;
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
