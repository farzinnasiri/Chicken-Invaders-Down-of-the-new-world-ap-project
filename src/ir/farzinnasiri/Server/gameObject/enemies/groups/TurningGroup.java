package ir.farzinnasiri.Server.gameObject.enemies.groups;

import ir.farzinnasiri.Server.GameState;
import ir.farzinnasiri.Server.gameObject.enemies.Group;
import ir.farzinnasiri.Server.gameObject.enemies.chickens.Chicken;
import ir.farzinnasiri.Utils.Constants;
import ir.farzinnasiri.Utils.UniqueId;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TurningGroup extends Group {

    private String kind;

    private List<Chicken> chickens;

    private GameState gameState;
    private double speed;

    public TurningGroup(int difficultyLevel) {
        super(UniqueId.getIdentifier(), difficultyLevel);

        gameState = GameState.getInstance();
    }


    @Override
    public void update(double elapsed) {
        Iterator<Chicken> chickenIterator = chickens.iterator();
        while (chickenIterator.hasNext()) {
            Chicken chicken = chickenIterator.next();
            if (!gameState.getChickens().contains(chicken)) {
                chickenIterator.remove();
            } else {

                if (chicken.getX() < Constants.WIDTH - Constants.CHICKEN_WIDTH && chicken.getY() <= 0) {
                    chicken.setNextPoint(chicken.getX() + 10, chicken.getY(), speed);
                } else if (chicken.getX() >= Constants.WIDTH - Constants.CHICKEN_WIDTH && chicken.getY()
                        < Constants.HEIGHT - Constants.CHICKEN_HEIGHT) {
                    chicken.setNextPoint(chicken.getX(), chicken.getY() + 10, speed);
                } else if (chicken.getY() >= Constants.HEIGHT - Constants.CHICKEN_HEIGHT && chicken.getX() > 0) {
                    chicken.setNextPoint(chicken.getX() - 10, chicken.getY(), speed);
                } else if (chicken.getY() >= 0 && chicken.getX() <= 0) {
                    chicken.setNextPoint(chicken.getX(), chicken.getY() - 10, speed);
                }

                chicken.update(elapsed);

            }
        }


    }

    @Override
    public void spanGroup() {
        switch (getDifficultyLevel()) {
            case 1:
                kind = "BLUE_CHICKENS";
                speed = 1d;
                break;
            case 2:
                kind = "PINK_CHICKENS";
                speed = 1.2d;

                break;
            case 3:
                kind = "GREEN_CHICKENS";
                speed = 1.5d;

                break;
            case 4:
                kind = "YELLOW_CHICKENS";
                speed = 2d;

                break;
        }
        creatInvaders();
        setSpanned(true);


    }

    @Override
    public void creatInvaders() {
        chickens = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            chickens.add(gameState.addChicken(getId(), kind, -Constants.CHICKEN_WIDTH * (8 - i), 0));
        }

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
}
