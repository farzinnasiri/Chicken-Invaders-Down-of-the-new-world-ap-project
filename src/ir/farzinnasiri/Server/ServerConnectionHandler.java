package ir.farzinnasiri.Server;


import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerConnectionHandler implements Runnable {

    private ServerSocket connection;
    private GameState gameState;

    private int port;
    private int maxCapacity;
    private int members;
    private boolean running;

    public ServerConnectionHandler(int port,int maxCapacity,int startingWave,int maxWaves) {
        this.port = port;
        this.maxCapacity = maxCapacity;

        gameState = GameState.getInstance();
        gameState.init();
        gameState.setPlayersCapacity(maxCapacity);
        gameState.setWavesBounds(startingWave,maxWaves);


        try {
            connection = new ServerSocket(port);
        } catch (IOException e) {
            if (!(e instanceof BindException)) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void run() {
        running = true;

        listenForClientRequest();


    }


    private void listenForClientRequest() {
        while ( running) {
            if (gameState.isGameFinished()) {
                running = false;
                try {
                    connection.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }catch (NullPointerException e2){

                }
                break;
            }
            try {
                Socket socket = connection.accept();


                ServerClient serverClient = new ServerClient(socket);
                synchronized (serverClient) {
                    serverClient.wait(100);

                    if (serverClient.isValid()) {
                        gameState.addClient(serverClient);
                    }
                    sendUpdatesToAllClients(serverClient);


                }


            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }catch (NullPointerException e2){
            }

        }
    }

    private void sendUpdatesToAllClients(ServerClient serverClient) {
        if (serverClient.isViewer() && serverClient.isValid()) {
            gameState.sendMessageToAll(serverClient.getName() + " is watching the game");
        } else if (serverClient.isValid()) {
            gameState.sendMessageToAll(serverClient.getName() + " has joined the game");
        }
    }


}
