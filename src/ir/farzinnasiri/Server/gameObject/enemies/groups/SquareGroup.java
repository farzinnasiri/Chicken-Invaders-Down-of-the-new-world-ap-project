package ir.farzinnasiri.Server.gameObject.enemies.groups;

import ir.farzinnasiri.Server.GameState;
import ir.farzinnasiri.Server.gameObject.enemies.Group;
import ir.farzinnasiri.Server.gameObject.enemies.chickens.Chicken;
import ir.farzinnasiri.Utils.Constants;
import ir.farzinnasiri.Utils.UniqueId;
import ir.farzinnasiri.Utils.Vector2D;

import java.util.ArrayList;
import java.util.List;


public class SquareGroup extends Group {
    private GameState gameState;

    private int rows;
    private int columns;
    private Vector2D base;
    private double velocity;
    private double speed;

    private String kind;

    private List<Chicken> chickens;

    public SquareGroup(int difficultyLevel) {
        super(UniqueId.getIdentifier(), difficultyLevel);
        chickens = new ArrayList<>();
        gameState = GameState.getInstance();
        velocity = 10;
        base = new Vector2D();



    }


    @Override
    public void update(double elapsed) {
        setAllDead(allDead());

        int j = 0;
        for (Chicken chicken : gameState.getChickens()) {
            if (chicken.getGroupId() == getId()) {
                chickens.set(j,chicken);
                j++;
            }
        }
        chickens = chickens.subList(0,j);
        putInPosition(elapsed);


    }

    private void putInPosition(double elapsed) {
        for (int i = 0; i < chickens.size(); i++) {
            Chicken chicken = chickens.get(i);
            if (base.getX() <= 0) {
                velocity = 10;
            } else if (base.getX() + Constants.CHICKEN_WIDTH*columns >= Constants.WIDTH) {
                velocity = -10;
            }
            base = base.add(new Vector2D(base.getX() + velocity, base.getY()).subtract(new Vector2D(base.getX(),
                    base.getY())).normalize().scale(speed*elapsed/30));
            chicken.setNextPoint(base.getX() + (i % columns) * Constants.CHICKEN_WIDTH,
                    base.getY() + (i / columns) * Constants.CHICKEN_HEIGHT,speed*2);

            chicken.update(elapsed);

        }


    }

    @Override
    public void spanGroup() {
        switch (getDifficultyLevel()) {
            case 1:
                kind = "BLUE_CHICKENS";
                rows = 5;
                columns = 5;
                speed = 1d;
                break;
            case 2:
                kind = "PINK_CHICKENS";
                rows = 5;
                columns = 6;
                speed = 1.2d;

                break;
            case 3:
                kind = "GREEN_CHICKENS";
                rows = 6;
                columns = 6;
                speed = 1.5d;

                break;
            case 4:
                kind = "YELLOW_CHICKENS";
                rows = 6;
                columns = 6;
                speed = 2d;

                break;
        }
        creatInvaders();
        setSpanned(true);

    }


    @Override
    public void creatInvaders() {
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < columns; c++) {
                gameState.addChicken(getId(), kind, -Constants.CHICKEN_WIDTH * (columns - c)
                        , 70 + (r) * Constants.CHICKEN_HEIGHT);
            }
        }
        for (Chicken chicken : gameState.getChickens()) {
            if (chicken.getGroupId() == getId()) {
                chickens.add(chicken);
            }
        }
        base.setX(-Constants.CHICKEN_WIDTH * columns);
        base.setY(70);


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
