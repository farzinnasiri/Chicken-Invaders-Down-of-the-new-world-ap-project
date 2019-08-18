package ir.farzinnasiri.Server;

public class ServerMain {

    public static void main(String[] args) {
        System.out.println("Server is started");

        int port;


        port = Integer.parseInt(args[0]);

        Thread serverConnectionThread;

        serverConnectionThread = new Thread(new ServerConnectionHandler(port, Integer.valueOf(args[1]),
                Integer.valueOf(args[2]),Integer.valueOf(args[3])),
                "Connection Thread");


        Thread serverThread = new Thread(new Server(), "Server Thread");
        serverConnectionThread.start();
        serverThread.start();


    }
}
