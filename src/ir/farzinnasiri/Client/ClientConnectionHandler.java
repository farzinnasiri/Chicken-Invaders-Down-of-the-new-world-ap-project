package ir.farzinnasiri.Client;

import ir.farzinnasiri.Client.audio.AudioManager;
import ir.farzinnasiri.Client.stateMachine.StateMachine;
import ir.farzinnasiri.Client.systemManager.PlayersManager;
import ir.farzinnasiri.Client.views.DrawableObject;
import ir.farzinnasiri.Client.views.GameObjects;
import ir.farzinnasiri.Client.views.MyPlayer;
import ir.farzinnasiri.Client.views.PlayerView;
import ir.farzinnasiri.Utils.Constants;
import ir.farzinnasiri.Utils.Timer;
import org.json.JSONObject;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Socket;
import java.util.*;

public class ClientConnectionHandler implements Runnable {


    String IPAddress;
    int port;
    Socket socket;
    private boolean running;
    private Thread thread;
    private Scanner input;
    private PrintWriter output;
    PlayersManager playersManager;
    private boolean connected;
    private String error;
    private Integer id;
    private GameObjects gameObjects;
    private ArrayList<String> serverAnswers;
    private StateMachine stateMachine;
    private AudioManager audioManager;


    private boolean watching;


    public ClientConnectionHandler(boolean watching, StateMachine stateMachine) {
        this.stateMachine = stateMachine;
        this.watching = watching;
        error = "";
        serverAnswers = new ArrayList<>();
        playersManager = PlayersManager.getInstance();
        gameObjects = GameObjects.getInstance();
        audioManager = AudioManager.getInstance();


    }

    public void init(String IPAddress, int port) {
        this.IPAddress = IPAddress;
        this.port = port;

    }


    public void start() {
        if (running) {
            return;
        }
        running = true;
        thread = new Thread(this, "Connection thread");
        thread.start();


    }

    public void stop() {
        if (!running) {
            return;
        }
        running = false;


    }


    @Override
    public void run() {
        try {

            socket = new Socket(IPAddress, port);
            input = new Scanner(socket.getInputStream());
            output = new PrintWriter(socket.getOutputStream(), true);
            if (!watching) {
                addPlayer();
            } else {
                output.println("VIEWER-" + playersManager.
                        getPlayerPropertiesJson(playersManager
                                .getPlayerProperties(playersManager.getCurrentPlayer())));
            }


            processCommands();

        } catch (ConnectException e1) {
            error = "There is no Server to Connect :'(";

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    private void processCommands() throws IOException {
        Timer timer = new Timer();

        while (input.hasNextLine() && running && !timer.timeEvent(10000)) {
            String command = input.nextLine();
            timer.resetTimer();

            if (command.equals("INVALID_USER_NAME")) {
                error = "this player is already in the game! try with another name";
                connected = false;
                socket.close();

            } else if (command.equals("VALID_USER_NAME")) {
                connected = true;
            } else if (command.startsWith("ID")) {
                id = Integer.valueOf(command.split(":")[1]);

            } else if (command.equals(String.valueOf(Constants.SERVER_FULL_ERROR))) {
                if (watching) {
                    serverAnswers.add(Constants.SERVER_FULL_PROMPT);
                    synchronized (this) {
                        notify();
                    }

                } else {
                    connected = false;
                    error = "So sorry dude!but the server is full ):";

                }

            } else if (command.startsWith("SAVE_WAVE")) {
                if (!watching) {
                    command = command.split(":")[1];
                    MyPlayer myPlayer = (MyPlayer) gameObjects.getPlayers().get(id);
                    myPlayer.saveProperties(Integer.parseInt(command));
                }

            } else if (command.startsWith("PLAYER_STATUS")) {

                resolvePlayers(getStandardCommand(command));
            } else if (command.startsWith("Message")) {
                command = command.split(":")[1];
                serverAnswers.add(command);
            } else if (command.startsWith("MESSAGE")) {
                command = command.split(":")[1];
                gameObjects.addMessage(command);

            } else if (command.startsWith("REMOVE_PLAYER")) {
                command = command.split(":")[1];
                gameObjects.removePlayer(Integer.parseInt(command));

            } else if (command.startsWith("REMOVE_WEAPON")) {
                command = getStandardCommand(command);
                JSONObject jsonObject = new JSONObject(command);

                gameObjects.removeWeapon((Integer) jsonObject.get("id"));

                if (jsonObject.has("explosion")) {
                    if ((boolean) jsonObject.get("explosion")) {
                        resolveSound("EXPLOSION");
                        gameObjects.addMissileExplosion();

                    }
                }
            } else if (command.startsWith("BOSS")) {
                command = getStandardCommand(command);
                resolveEnemy(command);

            } else if (command.startsWith("EXPLOSION")) {
                command = getStandardCommand(command);
                JSONObject explosionJson = new JSONObject(command);
                gameObjects.addExplosion(((Number) explosionJson.get("x")).intValue(),
                        ((Number) explosionJson.get("y")).intValue());
                resolveSound("EXPLOSION");


            } else if (command.startsWith("REMOVE_ENEMY")) {
                command = command.split(":")[1];
                gameObjects.removeEnemy(Integer.parseInt(command));
                resolveSound("CHICKEN");


            } else if (command.startsWith("REMOVE_EXTRA")) {
                command = command.split(":")[1];
                int id = Integer.parseInt(command.split(",")[0]);
                resolveSound(command.split(",")[1]);
                gameObjects.removeExtra(id);


            } else if (command.startsWith("PLAYING")) {
                command = command.split(":")[1];
                if (command.equals("true")) {
                    if (!gameObjects.isPlaying()) {
                        changeState(StateMachine.GAME_SCREEN);
                    }
                    gameObjects.setPlaying(true);
                    synchronized (this) {
                        notify();
                    }
                } else if (command.equals("false")) {
                    gameObjects.setPlaying(false);
                }


            } else if (command.startsWith("QUITE")) {
                command = getStandardCommand(command);
                JSONObject jsonObject = new JSONObject(command);
                if (jsonObject.get("state").equals("APPROVED")) {
                    synchronized (this) {
                        notify();
                    }

                }
                if (jsonObject.get("socket").equals("CLOSE")) {
                    closeSocket();
                }

            } else if (command.startsWith("LASER")) {
                resolveWeapon(getStandardCommand(command), "LASER");
            } else if (command.startsWith("MISSILE")) {
                resolveWeapon(getStandardCommand(command), "MISSILE");
            } else if (command.startsWith("CHICKEN")) {
                resolveEnemy(getStandardCommand(command));
            } else if (command.startsWith("EXTRA")) {
                resolveExtra(getStandardCommand(command));
            } else if (command.startsWith("PLAYER_LOST")) {
                command = command.split(":")[1];
                gameObjects.getPlayers().get(Integer.parseInt(command)).setLost(true);

                if ((Integer.parseInt(command) == id)) {
                    MyPlayer myPlayer = (MyPlayer) gameObjects.getPlayers().get(Integer.parseInt(command));
                    myPlayer.setLife(0);
                    myPlayer.saveProperties(Integer.parseInt(command));

                }
                gameObjects.removePlayer((Integer.parseInt(command)));

            } else if (command.equals("GAME_FINISHED")) {
                gameObjects.setFinished(true);
            }

        }

        gameObjects.setFinished(true);


    }

    private void resolveExtra(String command) {
        JSONObject extrasJson = new JSONObject(command);

        synchronized (gameObjects.getScreenExtraObjects()) {
            List<DrawableObject> extras = gameObjects.getScreenExtraObjects();

            for (DrawableObject extra : extras) {
                if (extra.getId() == (int) extrasJson.get("id")) {
                    extra.setX(((Number) extrasJson.get("x")).intValue());
                    extra.setY(((Number) extrasJson.get("y")).intValue());
                    return;
                }
            }

            DrawableObject newExtra = gameObjects.getExtrasDrawableObjectPool().checkOut();

            newExtra.init((int) extrasJson.get("id")
                    , "EXTRA"
                    , (String) extrasJson.get("kind")
                    , ((Number) extrasJson.get("x")).intValue()
                    , ((Number) extrasJson.get("y")).intValue()
                    , -1
                    , -1
                    , 0);
        }


    }

    private void resolveEnemy(String command) {
        JSONObject enemyJson = new JSONObject(command);
        synchronized (gameObjects.getEnemies()) {
            List<DrawableObject> enemies = gameObjects.getEnemies();

            for (DrawableObject enemy : enemies) {
                if (enemy.getId() == (int) enemyJson.get("id")) {
                    enemy.setX(((Number) enemyJson.get("x")).intValue());
                    enemy.setY(((Number) enemyJson.get("y")).intValue());
                    enemy.setRotation(((Number) enemyJson.get("rotation")).doubleValue());
                    return;
                }

            }

            DrawableObject newEnemy = gameObjects.getEnemiesDrawableObjectPool().checkOut();

            newEnemy.init((int) enemyJson.get("id")
                    , (String) enemyJson.get("object")
                    , (String) enemyJson.get("kind")
                    , ((Number) enemyJson.get("x")).intValue()
                    , ((Number) enemyJson.get("y")).intValue()
                    , Constants.CHICKEN_WIDTH
                    , Constants.CHICKEN_HEIGHT
                    , ((Number) enemyJson.get("rotation")).doubleValue()
            );

        }


    }


    private String getStandardCommand(String command) {
        String result = (command.substring(command.indexOf("{")));
        result = result.replaceAll("\\\\\"", "\"").trim();
        return result;

    }

    private void resolveWeapon(String weaponStatue, String object) {
        JSONObject weaponJson = new JSONObject(weaponStatue);
        synchronized (gameObjects.getWeapons()) {
            List<DrawableObject> weapons = gameObjects.getWeapons();

            for (DrawableObject weapon : weapons) {
                if (weapon.getId() == (int) weaponJson.get("id")) {
                    weapon.setX(((Number) weaponJson.get("x")).intValue());
                    weapon.setY(((Number) weaponJson.get("y")).intValue());

                    return;
                }
            }

            DrawableObject weapon = gameObjects.getWeaponsDrawableObjectPool().checkOut();

            weapon.init(
                    (int) weaponJson.get("id"),
                    object,
                    (String) weaponJson.get("color"),
                    ((Number) weaponJson.get("x")).intValue(),
                    ((Number) weaponJson.get("y")).intValue(),
                    (int) weaponJson.get("width"),
                    (int) weaponJson.get("height"),
                    ((Number) weaponJson.get("rotation")).doubleValue()

            );


        }

        resolveSound(object);

    }

    private void resolveSound(String object) {
        if (object.equals("LASER")) {
            audioManager.playSound("LASER_GUN", true);

        } else if (object.equals("MISSILE")) {
            audioManager.playSound("MISSILE", true);


        } else if (object.equals("EXPLOSION")) {
            audioManager.playSound("EXPLOSION", true);

        } else if (object.equals("CHICKEN")) {
            audioManager.playSound("CHICKEN", true);

        } else if (object.equals("POWERUP")) {
            audioManager.playSound("POWERUP", true);

        } else if (object.equals("EATING")) {
            audioManager.playSound("EATING", true);

        }

    }

    private void resolvePlayers(String status) {
        JSONObject player = new JSONObject(status);
        HashMap<Integer, PlayerView> players = gameObjects.getPlayers();
        if (players.containsKey(player.get("id"))) {


            if ((int) player.get("id") == id) {
                MyPlayer object = (MyPlayer) players.get(player.get("id"));
                object.setX(((Number) player.get("x")).intValue());
                object.setY(((Number) player.get("y")).intValue());
                if ((boolean) player.get("shield") && !object.isShied()) {
                    object.playShield(10000);
                }
                object.setDead(!(boolean) player.get("alive"));
                object.setOverheated((Boolean) player.get("overheat"));
                object.setMaxHeat((Integer) player.get("maxHeat"));
                object.setHeat((Integer) player.get("heat"));
                object.setFireLevel((Integer) player.get("fireLevel"));
                object.setFood((Integer) player.get("food"));
                object.setScore((Integer) player.get("score"));
                object.setMissile((Integer) player.get("missile"));
                object.setWeaponType((String) player.get("weaponType"));
                object.setLife((Integer) player.get("life"));
                object.setDurationInGame(((Number) player.get("duration")).longValue());
            } else {
                PlayerView object = players.get(player.get("id"));
                object.setX(((Number) player.get("x")).intValue());
                object.setY(((Number) player.get("y")).intValue());
                if ((boolean) player.get("shield") && !object.isShied()) {
                    object.playShield(10000);
                }

            }


        } else {
            int playerId = (int) player.get("id");
            PlayerView object;
            if (playerId == id) {
                object = new MyPlayer(playerId,
                        (String) player.get("name"),
                        (String) player.get("kind"),
                        (Integer) player.get("x"),
                        (Integer) player.get("y"),
                        (Integer) player.get("maxHeat"),
                        (String) player.get("exhaustColor"));
                if (watching) {
                    setWatching(false);
                    synchronized (this) {
                        notify();
                    }

                }


            } else {
                object = new PlayerView(playerId,
                        (String) player.get("name"),
                        (String) player.get("kind"),
                        (Integer) player.get("x"),
                        (Integer) player.get("y"),
                        (String) player.get("exhaustColor"));
            }
            gameObjects.addPlayer(playerId, object);


        }
    }


    public void sendCommend(String commend) {
        output.println(commend);

    }

    public boolean isConnected() {
        return connected;
    }

    public String getError() {
        return error;
    }


    public void getScreen() {
        sendCommend("GAME_STATE");

    }

    public Integer getId() {
        return id;
    }

    public boolean isWatching() {
        return watching;
    }

    public void setWatching(boolean watching) {
        this.watching = watching;
    }

    private void addPlayer() {
        output.println("ADD-" + playersManager.
                getPlayerPropertiesJson(playersManager
                        .getPlayerProperties(playersManager.getCurrentPlayer())));
    }

    synchronized public void requestGameEntering() {
        addPlayer();
    }

    public String getServerAnswer() {
        if (serverAnswers.size() > 0) {
            System.out.println(Arrays.toString(serverAnswers.toArray()));
            String latest = serverAnswers.get(0);
            serverAnswers.remove(0);
            return latest;
        } else {
            return null;
        }
    }


    private void changeState(int state) {
        stateMachine.setState(state);


    }

    private void closeSocket() {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
