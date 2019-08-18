package ir.farzinnasiri.Server.gameObject.weapons;

import ir.farzinnasiri.Server.gameObject.GameObject;
import ir.farzinnasiri.Utils.Vector2D;
import org.json.JSONObject;

import java.awt.*;

public class Laser extends GameObject {

    private double degree;
    private final double speed = 5d;
    private String color;
    private double power;

    private Vector2D position;
    private Vector2D velocity;

    private int playerId;


    public Laser(int id) {
        super(id);

        setWidth(12);
        setHeight(80);


    }

    public void init(int playerId, double x, double y, String color, double power, double degree) {
        setX(x);
        setY(y);
        this.color = color;
        this.degree = degree;
        this.power = power;
        this.playerId = playerId;

        position = new Vector2D(getX(),getY());
        velocity = new Vector2D(1,0).setDirection(-Math.PI/2 + degree);

        setAlive(true);

    }


    @Override
    public void update(double elapsed) {
        if (!isAlive()) {
            return;
        }

        position = position.add(velocity.scale(speed));

        setY(position.getY());
        setX(position.getX());
        isOutOfBounds();

    }

    @Override
    public Rectangle getRect() {
        return new Rectangle((int) getX(), (int) getY(), getWidth(), getHeight() - 30);

    }

    @Override
    public void destroy() {
        setAlive(false);
    }


    @Override
    protected void isOutOfBounds() {
        if (this.getY() <= -getHeight()) {
            destroy();
        }

    }

    public int getPlayerId() {
        return playerId;
    }

    public double getPower() {
        return power;
    }

    @Override
    public String toString() {
        JSONObject laser = new JSONObject();
        laser.put("id", getId());
        laser.put("color", color);
        laser.put("x", getX());
        laser.put("y", getY());
        laser.put("rotation", degree);
        laser.put("width", getWidth());
        laser.put("height", getHeight());
        laser.put("inUse", isAlive());
        return laser.toString();


    }
}
