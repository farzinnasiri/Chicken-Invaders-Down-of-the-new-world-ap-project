package ir.farzinnasiri.Server.gameObject.extras;

import ir.farzinnasiri.Server.gameObject.GameObject;
import ir.farzinnasiri.Utils.UniqueId;
import org.json.JSONObject;

import java.awt.*;

public class Coin extends GameObject {


    public Coin() {
        super(UniqueId.getIdentifier());
    }

    @Override
    public void update(double elapsed) {

    }

    @Override
    public Rectangle getRect() {
        return null;
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
        extra.put("object", "EXTRA");
        extra.put("kind", "EGG");

        return extra.toString();

    }
}
