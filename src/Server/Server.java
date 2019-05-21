package Server;

import java.util.ArrayList;

public class Server implements Runnable{
    private GameState gameState;



    public void Server(){
        gameState = GameState.getInstance();

    }

    @Override
    public void run() {

    }


}
