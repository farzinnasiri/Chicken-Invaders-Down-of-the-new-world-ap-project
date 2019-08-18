package ir.farzinnasiri.Client;

import ir.farzinnasiri.Client.audio.AudioManager;
import ir.farzinnasiri.Client.views.GameObjects;
import ir.farzinnasiri.Utils.Assets;
import ir.farzinnasiri.Utils.DataBase;
import javafx.embed.swing.JFXPanel;

import javax.swing.*;
import java.sql.SQLException;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;

public class ClientMain {

    private static Scanner scanner;

    public static void main(String[] args) {

        final CountDownLatch latch = new CountDownLatch(1);
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new JFXPanel(); // initializes JavaFX environment
                latch.countDown();
            }
        });
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        AudioManager audioManager = AudioManager.getInstance();

        Assets assets = Assets.getInstance();
        GameObjects gameObjects = GameObjects.getInstance();

        Client client = new Client();

        JFrame frame = new JFrame();
        frame.add(client);
        frame.pack();
        frame.setTitle("Chicken Invaders:Down of the new World");
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setVisible(true);



        client.start();




    }
}
