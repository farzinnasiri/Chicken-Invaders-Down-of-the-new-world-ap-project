package ir.farzinnasiri.Client.systemManager;


import ir.farzinnasiri.Utils.Assets;
import ir.farzinnasiri.Utils.DataBase;
import ir.farzinnasiri.Utils.JsonPropertiesParser;
import ir.farzinnasiri.Utils.PlayerProperties;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PlayersManager {
    private static PlayersManager playersManager;

    private String currentPlayer;
    private Assets assets;

    private JsonPropertiesParser jsonParser;
    private FileIO fileIO;

    private ConfigsManager configsManager;

    private DataBase dataBase;
    private boolean dataBaseConnected;



    private PlayersManager() {
        assets = Assets.getInstance();

        this.currentPlayer = assets.getBasicConfigProperty("currentPlayer");
        fileIO = new FileIO();
        jsonParser = new JsonPropertiesParser();

        configsManager = new ConfigsManager();

        dataBase = new DataBase();
        try {
            dataBase.setConnection();
            dataBaseConnected = true;
        } catch (Exception e) {
            dataBaseConnected = false;
            System.out.println("SORRY!!!:could not connect to database");
        }

        if(dataBaseConnected){
            for (String name : getAllPlayersNames()) {
                updatePlayerInDataBase(getPlayerProperties(name));

            }
            for (String playersName : dataBase.getPlayersNames()) {
                if(!getAllPlayersNames().contains(playersName)){
                    dataBase.removePlayer(playersName);
                }
            }

        }

    }

    public static PlayersManager getInstance() {
        if (playersManager == null) {
            playersManager = new PlayersManager();
        }
        return playersManager;
    }

    public void addNewPlayer(String name) {
        List<Integer> scores = new ArrayList<>();
        List<Integer> wavesPassed = new ArrayList<>();
        List<Long> durations = new ArrayList<>();
        scores.add(0);
        wavesPassed.add(0);
        durations.add((long) 0);
        PlayerProperties playerProperties = new PlayerProperties(name, scores, wavesPassed, durations, 5,
                3, 0, "RED", 0, 180, "blue", "blue"
        );
        savePlayerProperties(playerProperties);

        if (dataBaseConnected) addPlayerToDataBase(playerProperties);

    }

    private void addPlayerToDataBase(PlayerProperties playerProperties) {
        dataBase.addPlayer(playerProperties.getName(), playerProperties.getScores(), playerProperties.getWavesPassed(),
                playerProperties.getDurations(), playerProperties.getLife(), playerProperties.getMissile()
                , playerProperties.getFireLevel(), playerProperties.getWeaponType(), playerProperties.getFood()
                , playerProperties.getMaxHeat(), playerProperties.getSpaceshipColor(),
                playerProperties.getEngineColor());
    }

    public void newGame() {
        PlayerProperties playerProperties = getPlayerProperties(getCurrentPlayer());
        playerProperties.newGame();
        savePlayerProperties(playerProperties);

    }

    public void deletePlayer(String name) {
        fileIO.deleteFile("users", name);

        if (dataBaseConnected) deletePlayerFromDataBase(name);
    }

    private void deletePlayerFromDataBase(String name) {
        dataBase.removePlayer(name);
    }

    public PlayerProperties getPlayerProperties(String name) {
        return getProperties(name);

    }

    public PlayerProperties getProperties(String name) {
        String json = fileIO.readFile("users", name);
        return jsonParser.deserializeFromJson(json);


    }


    public void savePlayerProperties(PlayerProperties playerProperties) {
        String json = getPlayerPropertiesJson(playerProperties);
        fileIO.writeFile("users", playerProperties.getName(), json);
        if (dataBaseConnected) updatePlayerInDataBase(playerProperties);

    }

    private void updatePlayerInDataBase(PlayerProperties playerProperties) {
        dataBase.updatePlayer(playerProperties.getName(), playerProperties.getScores(), playerProperties.getWavesPassed(),
                playerProperties.getDurations(), playerProperties.getLife(), playerProperties.getMissile()
                , playerProperties.getFireLevel(), playerProperties.getWeaponType(), playerProperties.getFood()
                , playerProperties.getMaxHeat(), playerProperties.getSpaceshipColor(),
                playerProperties.getEngineColor());
    }

    public String getPlayerPropertiesJson(PlayerProperties playerProperties) {
        return jsonParser.serializeToJson(playerProperties.getName(), playerProperties);

    }


    public File[] getAllPlayers() {
        return fileIO.getAllFiles("users");

    }

    public ArrayList<String> getAllPlayersNames() {
        ArrayList<String> names = new ArrayList<>();

        for (File player : getAllPlayers()) {
            names.add(player.getName().replaceFirst("[.][^.]+$", ""));

        }
        return names;

    }


    public String getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(String player) {
        this.currentPlayer = player;

    }

    public void saveCurrentPlayer() {
        configsManager.saveProperty("basicConfigs", "currentPlayer", currentPlayer);
        assets.loadBasicConfigs();

    }

    public boolean isDataBaseConnected() {
        return dataBaseConnected;
    }


}
