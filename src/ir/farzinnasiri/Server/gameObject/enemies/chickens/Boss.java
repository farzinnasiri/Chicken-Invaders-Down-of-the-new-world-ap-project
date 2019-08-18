package ir.farzinnasiri.Server.gameObject.enemies.chickens;

import ir.farzinnasiri.Server.GameState;
import ir.farzinnasiri.Server.gameObject.GameObject;
import ir.farzinnasiri.Server.gameObject.enemies.EnemyType;
import ir.farzinnasiri.Utils.Constants;
import ir.farzinnasiri.Utils.UniqueId;
import ir.farzinnasiri.Utils.Vector2D;
import org.json.JSONObject;

import javax.swing.plaf.synth.SynthOptionPaneUI;
import java.awt.*;

public class Boss extends GameObject implements EnemyType {

    private GameState gameState;
    private int level;

    private Vector2D toNextPoint;
    private Vector2D velocity;
    private Vector2D position;

    private double nextPointY;
    private double nextPointX;
    private boolean moving;


    public Boss(int level) {
        super(UniqueId.getIdentifier());
        setLife(level * 250);
        this.level = level;
        setX((Constants.WIDTH - Constants.BOSS_WIDTH) / 2);
        setY(-300);
        setAlive(true);
        moving = true;


        position = new Vector2D(getX(), getY());
        toNextPoint = new Vector2D();


        gameState = GameState.getInstance();

    }


    @Override
    public void update(double elapsed) {
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
        return new Rectangle((int) getX() + 50, (int) getY() + 70, Constants.BOSS_WIDTH - 100,
                Constants.BOSS_HEIGHT - 120);
    }

    @Override
    public void destroy() {

    }

    @Override
    protected void isOutOfBounds() {

    }

    @Override
    public void shoot() {
        for (int i = 0; i < 5; i++) {
            gameState.addPowerUp(getX() + Constants.BOSS_WIDTH / 2 - 50 + i * 10, getY() + Constants.BOSS_WIDTH / 2 - 30 + i * 10);
        }

    }

    @Override
    public void kill(double weaponPower, int playerId) {
        setLife(getLife() - weaponPower);
        if (getLife() <= 0 && isAlive()) {
            shoot();
            gameState.addScore(playerId, level * 50000);
            gameState.sendExplosionCommand(getX(), getY());
            gameState.sendExplosionCommand(getX() + Constants.BOSS_WIDTH, getY());
            gameState.sendExplosionCommand(getX(), getY() + Constants.BOSS_HEIGHT);
            gameState.sendExplosionCommand(getX() + Constants.BOSS_WIDTH, getY() + Constants.BOSS_HEIGHT);
            gameState.sendExplosionCommand(getX() + Constants.BOSS_WIDTH / 2, getY() + Constants.BOSS_HEIGHT / 2);

            setAlive(false);
        }

    }

    public boolean isMoving() {
        return moving;
    }

    public void setMoving(boolean moving) {
        this.moving = moving;
    }

    @Override
    public void setNextPoint(double x, double y, double speed) {
        nextPointX = x;
        nextPointY = y;
        this.toNextPoint = new Vector2D(x, y).subtract(new Vector2D(getX(), getY())).normalize();
        velocity = toNextPoint.scale(speed);
        moving = true;

    }


    @Override
    public String toString() {
        JSONObject bossJson = new JSONObject();

        bossJson.put("id", getId());
        bossJson.put("x", this.getX());
        bossJson.put("y", this.getY());
        bossJson.put("kind", "BOSS2");
        bossJson.put("object", "ENEMY");
        bossJson.put("rotation", 0);


        return bossJson.toString();


    }
}
