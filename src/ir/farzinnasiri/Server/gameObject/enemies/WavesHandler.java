package ir.farzinnasiri.Server.gameObject.enemies;

import ir.farzinnasiri.Server.GameState;
import ir.farzinnasiri.Server.gameObject.enemies.groups.*;
import ir.farzinnasiri.Utils.DataBase;

import javax.xml.crypto.Data;
import java.sql.SQLException;
import java.util.ArrayList;

public class WavesHandler implements Runnable {


    private ArrayList<ArrayList<Group>> allWaves;

    private GameState gameState;

    private int maxWaves;

    private int wavePlaying;
    private boolean running;
    private boolean timeOut;
    private String[] messages;

    public WavesHandler(int maxWaves, int startingWave) {
        this.maxWaves = maxWaves;
        this.wavePlaying = startingWave - 1;
        gameState = GameState.getInstance();
        allWaves = new ArrayList<>();



        loadWaves();


    }

    private void loadWaves() {
        DataBase db = new DataBase();
        try {
            db.setConnection();
            for(int wave = 0 ; wave < 20; wave++){
                String name = db.getWaveName(wave+1);
                Group group = null;

                switch (name){
                    case "SquareGroup":
                        group = new SquareGroup((wave/5)+1 );
                    break;
                    case "SuicideGroup":
                        group = new SuicideGroup((wave/5)+1);
                        break;
                    case "CircularGroup":
                        group = new CircularGroup((wave/5)+1);
                        break;
                    case "RotationalGroup":
                        group = new RotationalGroup((wave/5)+1);
                        break;
                    case "BossGroup":
                        group = new BossGroup((wave/5)+1);
                        break;

                }
                ArrayList<Group> groups = new ArrayList<>();
                groups.add(group);
                allWaves.add(groups);

            }



        } catch (SQLException e) {
            e.printStackTrace();
        ArrayList<Group> wave1 = new ArrayList<>();
        wave1.add(new SquareGroup(1));
        allWaves.add(wave1);


        ArrayList<Group> wave2 = new ArrayList<>();
        wave2.add(new SuicideGroup(1));
        allWaves.add(wave2);

        ArrayList<Group> wave3 = new ArrayList<>();
        wave3.add(new CircularGroup(1));
        allWaves.add(wave3);

        ArrayList<Group> wave4 = new ArrayList<>();
        wave4.add(new RotationalGroup(1));
        allWaves.add(wave4);

        ArrayList<Group> wave5 = new ArrayList<>();
        wave5.add(new BossGroup(1));
        allWaves.add(wave5);

        ArrayList<Group> wave6 = new ArrayList<>();
        wave6.add(new RotationalGroup(2));
        allWaves.add(wave6);

        ArrayList<Group> wave7 = new ArrayList<>();
        wave7.add(new CircularGroup(2));
        allWaves.add(wave7);

        ArrayList<Group> wave8 = new ArrayList<>();
        wave8.add(new SquareGroup(2));
        allWaves.add(wave8);

        ArrayList<Group> wave9 = new ArrayList<>();
        wave9.add(new SquareGroup(2));
        allWaves.add(wave9);


        ArrayList<Group> wave10 = new ArrayList<>();
        wave10.add(new BossGroup(2));
        allWaves.add(wave10);

        ArrayList<Group> wave11 = new ArrayList<>();
        wave11.add(new SuicideGroup(3));
        allWaves.add(wave11);

        ArrayList<Group> wave12 = new ArrayList<>();
        wave12.add(new CircularGroup(3));
        allWaves.add(wave12);


        ArrayList<Group> wave13 = new ArrayList<>();
        wave13.add(new RotationalGroup(3));
        allWaves.add(wave13);

        ArrayList<Group> wave14 = new ArrayList<>();
        wave14.add(new SquareGroup(3));
        allWaves.add(wave14);

        ArrayList<Group> wave15 = new ArrayList<>();
        wave15.add(new BossGroup(3));
        allWaves.add(wave15);

        ArrayList<Group> wave16 = new ArrayList<>();
        wave16.add(new SquareGroup(4));
        allWaves.add(wave16);

        ArrayList<Group> wave17 = new ArrayList<>();
        wave17.add(new CircularGroup(4));
        allWaves.add(wave17);

        ArrayList<Group> wave18 = new ArrayList<>();
        wave18.add(new SuicideGroup(4));
        allWaves.add(wave18);

        ArrayList<Group> wave19 = new ArrayList<>();
        wave19.add(new RotationalGroup(4));
        allWaves.add(wave19);

        ArrayList<Group> wave20 = new ArrayList<>();
        wave20.add(new BossGroup(4));
        allWaves.add(wave20);
        }



    }

    public void update(double elapsed) {
        if (wavePlaying < maxWaves && !timeOut && !(wavePlaying > allWaves.size() - 1)) {
            ArrayList<Group> groups = allWaves.get(wavePlaying);
            for (Group group : groups) {
                if (group.isSpanned()) {
                    group.update(elapsed);

                }
            }
        }

    }

    public void addNewGroup(Class c) {
        try {
            Object group = Class.forName("TurningGroup").newInstance();
            System.out.println(group instanceof Group);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }


    @Override
    public void run() {
        running = true;
        while (running) {
            if (Thread.interrupted() || !gameState.isGame()) {

                return;
            }
            int deadGroups = 0;
            for (Group group : allWaves.get(wavePlaying)) {
                if (!group.isSpanned()) {
                    group.spanGroup();
                    gameState.sendCommandToAll("MESSAGE:" + "WAVE " + (wavePlaying + 1));
                }
                if (group.allDead()) {
                    deadGroups++;
                }

            }

            if (deadGroups == allWaves.get(wavePlaying).size()) {
                wavePlaying++;
                if (wavePlaying > maxWaves - 1) {
                    running = false;
                    gameState.sendCommandToAll("MESSAGE:FINISHED!");
                    gameState.stopGame();

                } else {
                    if (!gameState.isMultiplayer()) {
                        gameState.sendCommandToAll("SAVE_WAVE:" + (wavePlaying));
                    }
                    try {
                        timeOut = true;
                        gameState.sendNewPlayersIn();
                        Thread.sleep(1000);
                        timeOut = false;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }


                }
            }


        }


    }


}
