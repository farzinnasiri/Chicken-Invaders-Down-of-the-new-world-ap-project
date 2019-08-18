package ir.farzinnasiri.Server.gameObject.weapons;

import ir.farzinnasiri.Server.GameState;
import ir.farzinnasiri.Server.gameObject.GameObject;
import ir.farzinnasiri.Utils.Constants;
import ir.farzinnasiri.Utils.Timer;
import ir.farzinnasiri.Utils.UniqueId;
import ir.farzinnasiri.Utils.Vector2D;
import org.json.JSONObject;

import java.awt.*;

public class Missile extends GameObject {
    private double angle;

    private Vector2D position;
    private Vector2D velocity;
    private Vector2D toCenter;
    private Timer timer;
    private boolean exploded;
    private int power;
    private int playerId;


    public Missile(int playerId, double x, double y) {
        super(UniqueId.getIdentifier());

        setX(x);
        setY(y);
        setHeight(15);
        setWidth(50);
        setAlive(true);

        this.playerId = playerId;
        power = 10;


        timer = new Timer();


        toCenter = ((new Vector2D(Constants.WIDTH / 2, Constants.HEIGHT / 2)).subtract(
                new Vector2D(x + getWidth() / 2, y + getHeight() / 2)
        )).normalize();


        position = new Vector2D(x + getWidth() / 2, y + getHeight() / 2).add(toCenter
                .scale(70));
        angle = toCenter.getAngle() + Math.PI / 2;

        if (x >= Constants.WIDTH / 2) {
            angle = -1 * angle;
        }


        velocity = toCenter.scale(2);


    }


    @Override
    public void update(double elapsed) {
        if (exploded) return;
        if (isCentered()) {
            exploded = true;
            destroy();


        } else {
            position = position.add(velocity.scale(elapsed));
            setX(position.getX());
            setY(position.getY());
        }

        isOutOfBounds();


    }

    @Override
    public Rectangle getRect() {
        return new Rectangle(100, 50, Constants.WIDTH - 200, Constants.HEIGHT - 100);

    }


    @Override
    public void destroy() {

        setAlive(false);

    }

    @Override
    protected void isOutOfBounds() {
        if (position.getX() > Constants.WIDTH ||
                position.getY() > Constants.HEIGHT ||
                position.getX() < 0 ||
                position.getY() < 0) {
            destroy();
        }

    }

    private boolean isCentered() {
        Rectangle missileBox = new Rectangle((int) position.getX(), (int) position.getY(), getWidth(), getHeight());
        if (missileBox.contains(Constants.WIDTH / 2, Constants.HEIGHT / 2)) {
            timer.resetTimer();
            return true;
        }

        return false;


    }

    public boolean hasExploded() {
        return exploded;

    }

    public int getPower() {
        return power;
    }

    public int getPlayerId() {
        return playerId;
    }


    @Override
    public String toString() {
        JSONObject missile = new JSONObject();
        missile.put("id", getId());
        missile.put("x", getX());
        missile.put("y", getY());
        missile.put("color", "");
        missile.put("rotation", angle);
        missile.put("width", getWidth());
        missile.put("height", getHeight());
        missile.put("inUse", isAlive());
        missile.put("explosion", exploded);
        return missile.toString();
    }
}
