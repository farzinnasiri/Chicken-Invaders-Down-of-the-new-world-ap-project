package ir.farzinnasiri.Server.gameObject.enemies.chickens;

import ir.farzinnasiri.Server.GameState;
import ir.farzinnasiri.Server.gameObject.GameObject;
import ir.farzinnasiri.Server.gameObject.enemies.EnemyType;
import ir.farzinnasiri.Utils.Constants;
import ir.farzinnasiri.Utils.Timer;
import ir.farzinnasiri.Utils.UniqueId;
import ir.farzinnasiri.Utils.Vector2D;
import org.json.JSONObject;

import java.awt.*;
import java.util.Random;

public class Chicken extends GameObject implements EnemyType {

    private Random randomFood;
    private Random randomEgg;
    private Random randomPowerUp;
    private Timer eggingTimer;


    private boolean moving;

    private int eggSpeed;
    private int eggingTimerStep;
    private int eggChance;

    private double nextPointY;
    private double nextPointX;

    private int groupId;


    private Vector2D toNextPoint;
    private Vector2D velocity;
    private Vector2D position;

    private GameState gameState;
    private int food;

    private int[] lifes;


    public Chicken() {
        super(UniqueId.getIdentifier());
        lifes = new int[]{2,3,5,8};


    }

    public void init(int groupId, String kind, double x, double y) {
        setX(x);
        setY(y);
        setAlive(true);

        this.groupId = groupId;

        gameState = GameState.getInstance();
        eggingTimerStep = 1000;

        position = new Vector2D(getX(), getY());
        toNextPoint = new Vector2D();

        randomEgg = new Random();
        randomFood = new Random();
        randomPowerUp = new Random();
        eggingTimer = new Timer();

        setKind(kind);
        resolveKind(kind);

    }

    private void resolveKind(String kind) {
        int playersNumber = gameState.getSpaceShips().size();
        if (kind.equals("BLUE_CHICKENS")) {
            setLife(lifes[0] * Math.sqrt(playersNumber));
            food = 1;
            eggSpeed = 1;
            eggChance = 60;
        } else if (kind.equals("GREEN_CHICKENS")) {
            food = 3;
            eggSpeed = 2;
            eggChance = 20;
            setLife(lifes[3] * Math.sqrt(playersNumber));
        } else if (kind.equals("PINK_CHICKENS")) {
            food = 1;
            eggSpeed = 1;
            eggChance = 50;
            setLife(lifes[1] * Math.sqrt(playersNumber));
        } else if (kind.equals("YELLOW_CHICKENS")) {
            food = 2;
            eggSpeed = 2;
            eggChance = 30;
            setLife(lifes[2] * Math.sqrt(playersNumber));

        }


    }


    @Override
    public void shoot() {
        if (eggingTimer.timeEvent(eggingTimerStep)) {
            if (randomEgg.nextInt(eggChance) == 0) {
                gameState.addEgg(getX(), getY(), eggSpeed);
            }
        }

    }


    @Override
    public void kill(double weaponPower, int playerId) {
        setLife(getLife() - weaponPower);
        if (getLife() <= 0) {
            gameState.addScore(playerId, lifes[food] * 500);
            gameState.sendExplosionCommand(getX(), getY());
            setAlive(false);
        }
        if (!isAlive() && (randomFood.nextInt(6) == 0)) {

            gameState.addFood(food, getX(), getY());
        }
        if (!isAlive() && (randomPowerUp.nextInt(25) == 0)) {
            gameState.addPowerUp(getX(), getY());
        }

    }


    @Override
    public void update(double elapsed) {
        shoot();

        if ((position.getX() >= nextPointX - 5 && position.getX() <= nextPointX + 5)
                && (position.getY() >= nextPointY - 5 && position.getY() <= nextPointY + 5)) {
            moving = false;

        } else {
            position = position.add(velocity.scale(elapsed));
            moving = true;
        }

        setX(position.getX());
        setY(position.getY());


    }

    @Override
    public Rectangle getRect() {
        return new Rectangle((int) getX() + 30, (int) getY(),
                Constants.CHICKEN_WIDTH - 30,
                Constants.CHICKEN_HEIGHT - 20);
    }

    @Override
    public void destroy() {

    }

    @Override
    protected void isOutOfBounds() {

    }

    @Override
    public void setNextPoint(double x, double y, double speed) {
        nextPointX = x;
        nextPointY = y;
        this.toNextPoint = new Vector2D(x, y).subtract(new Vector2D(getX(), getY())).normalize();
        velocity = toNextPoint.scale(speed);
        moving = true;

    }


    public int getGroupId() {
        return groupId;
    }

    public boolean isMoving() {
        return moving;
    }

    @Override
    public String toString() {
        JSONObject chicken = new JSONObject();
        chicken.put("id", getId());
        chicken.put("object", "ENEMY");
        chicken.put("kind", getKind());
        chicken.put("x", getX());
        chicken.put("y", getY());
        chicken.put("rotation",0);

        return chicken.toString();
    }
}
