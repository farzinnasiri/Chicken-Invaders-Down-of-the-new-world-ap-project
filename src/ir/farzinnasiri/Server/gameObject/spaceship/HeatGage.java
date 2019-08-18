package ir.farzinnasiri.Server.gameObject.spaceship;


import ir.farzinnasiri.Server.GameState;

import java.util.Timer;
import java.util.TimerTask;

public class HeatGage {
    private int max;
    private int daceyRate;
    private int temperature;



    private int heat;
    private static boolean overHeated;
    private Timer timer = new Timer();
    private int playerId;
    private GameState gameState;


    public HeatGage(int playerId, int max) {
        this.playerId = playerId;
        this.max = max;
        daceyRate = 72;
        heat = 5;

        gameState = GameState.getInstance();

    }


    public void update(double elapsed) {
        boolean shooting = gameState.isSpaceShipShooting(playerId);
        if (!shooting || (overHeated)) {
            if (temperature < 0) {
                temperature = 0;

            }
            temperature -= (daceyRate * elapsed / 1000);
        }
        if (temperature >= max) {
            overHeated = true;

        }
        if (overHeated) {
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    overHeated = false;
                }
            }, 4 * 1000);

        }

    }


    public void addHeat() {
        if (!overHeated) {
            temperature += heat;
        }

    }

    public boolean isOverHeated() {
        return overHeated;
    }

    public void increasMax(int amount) {
        max += amount;
    }

    public int getTemperature() {
        return temperature;
    }


}
