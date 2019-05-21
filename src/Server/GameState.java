package Server;

import java.util.ArrayList;

public class GameState {
    private static GameState gameState;
    private ArrayList<ServerClient> clientsList;


    public GameState(){
        clientsList = new ArrayList<>();
    }


    public static GameState getInstance(){
        if(gameState == null){
            gameState = new GameState();

        }

        return gameState;

    }



    public ArrayList<ServerClient> getClientsList() {
        return clientsList;
    }

    public void addClient(ServerClient serverClient) {
        clientsList.add(serverClient);

    }







}
