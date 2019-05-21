package Server;

public class ServerMain {

    public static void main(String[] args) {

        int port = 8888;

        if(args.length == 1){
            port = Integer.parseInt(args[0]);
        }

        Thread serverThread = new Thread(new Server());
        Thread serverConnectionThread = new Thread(new ServerTCPConnection(port));

        serverConnectionThread.start();
        serverThread.start();

    }
}
