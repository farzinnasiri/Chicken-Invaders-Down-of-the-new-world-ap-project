package ir.farzinnasiri.Server;


import ir.farzinnasiri.Server.gameObject.spaceship.SpaceShip;
import ir.farzinnasiri.Utils.Constants;
import ir.farzinnasiri.Utils.JsonPropertiesParser;
import ir.farzinnasiri.Utils.PlayerProperties;
import ir.farzinnasiri.Utils.UniqueId;
import org.json.JSONObject;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ServerClient {

    private PrintWriter output;
    private Scanner input;
    private int id;


    PlayerProperties playerProperties;
    private String name;
    private GameState gameState;
    private boolean viewer;


    Socket socket;

    Thread receive, send;
    private boolean valid;
    private boolean quited;
    private boolean master;
    private boolean playing;


    public ServerClient(Socket socket) {
        System.out.println("a new client");
        this.socket = socket;
        gameState = GameState.getInstance();
        name = "";

        try {

            output = new PrintWriter(socket.getOutputStream(), true);
            input = new Scanner(socket.getInputStream());
            receive();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void receive() {

        receive = new Thread("Receive") {

            public void run() {

                while (input.hasNextLine()) {
                    String commend = input.nextLine();

                    process(commend);

                    if (quited) break;


                }
                removeThisClient();


            }
        };

        receive.start();

    }

    private void removeThisClient() {
        gameState.removeClient(id);
        if (!isViewer()) gameState.removeSpaceShip(id);
        gameState.sendMessageToAll(ServerClient.this.getName() + " is out");
        gameState.sendCommandToAll("REMOVE_PLAYER:" + id);
        UniqueId.returnId(id);

    }

    private void process(String commend) {
        if (commend.startsWith("ADD")) {
            commend = commend.split("-")[1];
            if (!isViewer()) resolveNewClient(commend);
            if (valid) {
                synchronized (this) {
                    if (!gameState.isFull()) {
                        addPlayer(commend);
                        sendGameState(output);
                        if (isViewer()) {
                            gameState.sendMessageToAll(getName() + " has joined the game");
                        }
                        viewer = false;
                        playing = true;

                    } else {
                        if (!isViewer()) valid = false;
                        sendCommand(Constants.SERVER_FULL_ERROR);
                    }
                    this.notify();


                }
            }

        } else if (commend.startsWith("VIEWER")) {
            commend = commend.split("-")[1];
            resolveNewClient(commend);
            if (valid) {
                synchronized (this) {
                    viewer = true;
                    sendGameState(output);
                    this.notify();
                }
            }

        } else if (commend.equals("GAME_STATE")) {
            sendGameState(output);

        } else if (commend.startsWith("PLAYER_COORDINATE")) {
            JSONObject json = new JSONObject(getStandardCommand(commend));
            resolvePosition(json);

        } else if (commend.equals("ESCAPE_PRESSED")) {
            if (!isViewer()) {
                if (playing) {
                    playing = false;
                    gameState.setPlaying(false);
                    gameState.sendCommandToAll("PLAYING:" + false);
                } else {
                    playing = true;
                    gameState.setPlaying(true);
                }
            } else {
                sendCommand("PLAYING:" + false);
            }

        } else if (commend.equals("QUIT")) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("state", "APPROVED");
            if (gameState.getClientsList().size() == 1) {
                jsonObject.put("socket", "CLOSE");
            } else {
                jsonObject.put("socket", "OPEN");
            }
            if (master) {
                gameState.sendCommandToAll("QUITE:" + jsonObject.toString());

            } else {
                sendCommand("QUITE:" + jsonObject.toString());
            }
            quited = true;

        } else if (commend.startsWith("SHOOT")) {
            JSONObject json = new JSONObject(getStandardCommand(commend));
            if (json.has("laser")) {
                gameState.setSpaceShipShooting(id, (boolean) json.get("laser"), false);
            }
            if (json.has("missile")) {
                gameState.setSpaceShipShooting(id, (boolean) json.get("missile"), true);
            }
        } else if (commend.equals("MASTER")) {
            master = true;
        } else if (commend.equals("START")) {
            if (master && !gameState.isGame()) {
                gameState.startGame();
            }
        } else if (commend.equals("MULTIPLAYER")) {
            if (master) {
                gameState.setMultiplayer(true);
            }
        } else if (commend.equals("SINGLEPLAYER")) {
            if (master) {
                gameState.setMultiplayer(false);
            }
        }

    }

    public synchronized void promptMessage(String message) {
        output.println("Message:" + message);
    }

    private void resolveNewClient(String commend) {
        playerProperties = new JsonPropertiesParser().deserializeFromJson(commend);
        for (ServerClient serverClient : GameState.getInstance().getClientsList()) {
            if (playerProperties.getName().equals(serverClient.getName())) {
                sendCommand("INVALID_USER_NAME");
                valid = false;
                return;
            }

        }
        name = playerProperties.getName();
        sendCommand("VALID_USER_NAME");
        valid = true;
        id = UniqueId.getIdentifier();
        sendClientId(id);
        sendGameState(output);

    }

    private String getStandardCommand(String command) {
        String result = (command.substring(command.indexOf("{")));
        result = result.replaceAll("\\\\\"", "\"").trim();
        return result;

    }

    private void sendClientId(int id) {
        output.println("ID:" + id);

    }

    private void sendGameState(PrintWriter output) {
        gameState.getState(output);


    }

    public void sendCommand(Object command) {
        output.println(command);

    }

    private void addPlayer(String command) {
        JSONObject player = new JSONObject(command);
        SpaceShip spaceShip = new SpaceShip((String) player.get("name"),
                (String) player.get("spaceshipColor")
                , (Constants.WIDTH - 70) / 2,
                Constants.HEIGHT - 110,
                Constants.SPACESHIP_WIDTH, Constants.SPACESHIP_HEIGHT,
                (Integer) player.get("life")
                , id,
                (Integer) player.get("maxHeat"),
                (String) player.get("engineColor"),
                (String) player.get("weaponType"),
                (Integer) player.get("fireLevel"),
                (Integer) player.get("food"),
                (Integer) player.get("missile"));
        int score = (Integer) player.getJSONArray("scores").get(player.getJSONArray("scores").length() - 1);
        long statTimer = ((Number) player.getJSONArray("durations").get(player.getJSONArray("durations")
                .length() - 1)).longValue();

        spaceShip.setScore(score);
        spaceShip.setStartTime(statTimer);

        gameState.addPlayer(spaceShip);

        if (gameState.getSpaceShips().size() == 1) {
            master = true;
        }
    }

    private void resolvePosition(JSONObject json) {
        for (SpaceShip spaceShip : gameState.getSpaceShips()) {
            if (spaceShip.getId() == (int) json.get("id")) {
                spaceShip.setX(((Number) json.get("x")).doubleValue());
                spaceShip.setY(((Number) json.get("y")).doubleValue());
            }
        }

    }

    public boolean isValid() {
        return valid;
    }

    public String getName() {
        return name;
    }

    public boolean isViewer() {
        return viewer;
    }

    public int getId() {
        return id;
    }

    public boolean isMaster() {
        return master;
    }

    public boolean isPlaying() {
        return playing;
    }

    public void setPlaying(boolean playing) {
        this.playing = playing;
    }


}
