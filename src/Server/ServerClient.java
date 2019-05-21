package Server;

import jdk.swing.interop.SwingInterOpUtils;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ServerClient {

    private int id;

    ObjectInputStream in;
    ObjectOutputStream out;
    Socket socket;

    Thread receive,send;

    public ServerClient(Socket socket,int id){
        this.socket = socket;
        this.id = id;



        try {

            System.out.println("it");
        out =new ObjectOutputStream(socket.getOutputStream());
        in =new ObjectInputStream(socket.getInputStream());

            System.out.println("is");

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
