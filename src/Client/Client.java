package Client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Client implements Runnable{

    private  ObjectInputStream in;
    private  ObjectOutputStream out;


    public Client(Socket socket){
        try {
            out =new ObjectOutputStream(socket.getOutputStream());
            in =new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (true){
        }

    }
}
