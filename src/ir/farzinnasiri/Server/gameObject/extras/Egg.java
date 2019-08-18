package ir.farzinnasiri.Server.gameObject.extras;

import ir.farzinnasiri.Server.gameObject.GameObject;
import ir.farzinnasiri.Utils.Constants;
import ir.farzinnasiri.Utils.UniqueId;
import org.json.JSONObject;

import java.awt.*;

public class Egg extends GameObject {
    private int speed;


    public Egg() {
        super(UniqueId.getIdentifier());

    }

    public void init(double x, double y, int speed) {
        setX(x);
        setY(y);
        setAlive(true);
        this.speed = speed;
    }

    @Override
    public void update(double elapsed) {
        if (getY() < Constants.HEIGHT) {
            setY(getY() + speed * elapsed);
        } else {
            setAlive(false);
        }

    }

    @Override
    public Rectangle getRect() {
        return new Rectangle((int) getX() + 10, (int) getY()+ 5, 19, 27);
    }

    @Override
    public void destroy() {

    }

    @Override
    protected void isOutOfBounds() {

    }

    @Override
    public String toString() {
        JSONObject extra = new JSONObject();
        extra.put("id", getId());
        extra.put("x", getX());
        extra.put("y", getY());
        extra.put("object", "EGG");
        extra.put("kind", "EGG");

        return extra.toString();

    }
}
