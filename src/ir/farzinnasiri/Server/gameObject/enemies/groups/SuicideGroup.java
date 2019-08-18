package ir.farzinnasiri.Server.gameObject.enemies.groups;

import ir.farzinnasiri.Server.GameState;
import ir.farzinnasiri.Server.gameObject.enemies.Group;
import ir.farzinnasiri.Server.gameObject.enemies.chickens.Chicken;
import ir.farzinnasiri.Utils.Constants;
import ir.farzinnasiri.Utils.Timer;
import ir.farzinnasiri.Utils.UniqueId;
import ir.farzinnasiri.Utils.Vector2D;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SuicideGroup extends Group {
    private Random randomPosition;
    private Timer timer;

    private Vector2D playerPosition;

    private List<Chicken> chickens;
    private int maxChickens;
    private int aliveChickens;
    private String type;
    private Random randomAttack;
    private GameState gameState;

    public SuicideGroup(int difficultlyLevel) {
        super(UniqueId.getIdentifier(), difficultlyLevel);

        randomPosition = new Random(System.currentTimeMillis());
        playerPosition = new Vector2D(0, 0);
        randomAttack = new Random(System.currentTimeMillis());

        gameState = GameState.getInstance();

        chickens = new ArrayList<>();


        timer = new Timer();
    }


    @Override
    public void update(double elapsed) {
        setAllDead(allDead());
        int i = 0;
        for (Chicken chicken : gameState.getChickens()) {
            if (chicken.getGroupId() == getId()) {
                chickens.set(i, chicken);
                chicken.update(elapsed);
                i++;
            }
        }
        aliveChickens = chickens.size();

        if (timer.timeEvent(15000)) {
            randomTarget();
        }


    }


    private void randomTarget() {
        if (aliveChickens > 0) {

            int randomAttacker = randomAttack.nextInt(aliveChickens);

            int j = 0;
            for (Chicken chicken : chickens) {
                if (chicken.isAlive()) {
                    double x, y;
                    double speed;
                    if (j == randomAttacker) {

                        setPlayerPosition();

                        x = playerPosition.getX();
                        y = playerPosition.getY();

                        speed = 4;
                        j = -1;

                    } else {

                        x = randomPosition.nextInt(Constants.WIDTH - Constants.CHICKEN_WIDTH);
                        y = randomPosition.nextInt(Constants.HEIGHT - Constants.CHICKEN_HEIGHT - 100);
                        speed = 1;

                        j++;
                    }
                    chicken.setNextPoint(x, y, speed);
                }
            }
        }


    }

    private void setPlayerPosition() {
        Random random = new Random(System.nanoTime());
        int r = random.nextInt(gameState.getSpaceShips().size());

        playerPosition.setX(gameState.getSpaceShips().get(r).getX());
        playerPosition.setY(gameState.getSpaceShips().get(r).getY());


    }

    @Override
    public void spanGroup() {
        switch (getDifficultyLevel()) {
            case 1:
                maxChickens = 10;
                type = "BLUE_CHICKENS";
                break;
            case 2:
                maxChickens = 15;
                type = "PINK_CHICKENS";
                break;
            case 3:
                maxChickens = 20;
                type = "YELLOW_CHICKENS";
                break;
            case 4:
                maxChickens = 20;
                type = "GREEN_CHICKENS";
                break;
        }
        aliveChickens = maxChickens;
        creatInvaders();
        timer.resetTimer();
        setSpanned(true);


    }

    @Override
    public void creatInvaders() {
        Random random = new Random(System.nanoTime());

        for (int i = 0; i < maxChickens; i++) {

            int chance = random.nextInt(21);
            int x, y;

            if (chance > 10) {

                x = -1 * randomPosition.nextInt(400) - 100;

            } else {
                x = randomPosition.nextInt(500) + Constants.WIDTH;
            }
            y = randomPosition.nextInt(Constants.HEIGHT);

            gameState.addChicken(getId(), type, x, y);


        }
        for (Chicken chicken : gameState.getChickens()) {
            if (chicken.getGroupId() == getId()) {
                chickens.add(chicken);
            }
        }
        randomTarget();


    }


    @Override
    public boolean allDead() {
        synchronized (chickens) {
            for (Chicken chicken : chickens) {
                if (chicken.isAlive()) {
                    return false;
                }
            }
            return true;
        }
    }
    @Override
    public String toString() {
        return "SuicideGroup";
    }

}
