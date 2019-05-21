package Server;

import Utils.UniqueId;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerTCPConnection implements Runnable{

    private ServerSocket connection;
    private GameState gameState;

    private int port;

    public ServerTCPConnection(int port){
        this.port = port;

        gameState = GameState.getInstance();

        try {
            connection = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void run() {
        listenForClientRequest();


    }



    private void listenForClientRequest(){
        while (!Thread.interrupted()){
            System.out.println("yap");

            try {
                Socket socket = connection.accept();
                System.out.println(socket.getInetAddress()+"-"+
                socket.getLocalAddress()+"-"+
                socket.getLocalPort()+"-"+
                socket.getPort()+"-");
                gameState.addClient(new ServerClient(socket,UniqueId.getIdentifier()));
                System.out.println("here");
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }



}
