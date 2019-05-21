package Client;

import java.io.IOException;
import java.net.Socket;

public class ClientTCPConnection implements Runnable{


    String IPAddress;
    int port;
    Socket socket;


    public ClientTCPConnection(String IPAddress, int port) {
        this.IPAddress = IPAddress;
        this.port = port;

    }


    @Override
    public void run() {
        try {
            socket = new Socket(IPAddress,port);
             Thread client = new Thread(new Client(socket));
             client.start();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
