package Client;

import java.util.Scanner;

public class ClientMain {
    private static Scanner scanner;

    public static void main(String[] args) {

//        scanner = new Scanner(System.in);
//
//        System.out.println("Enter port:");
//        int port = scanner.nextInt();
//        System.out.println("Enter ip");
//
//        String IPAddress = scanner.next();

        Thread clientConnectionThread= new Thread(new ClientTCPConnection("localhost",8888));


        clientConnectionThread.start();


    }
}
