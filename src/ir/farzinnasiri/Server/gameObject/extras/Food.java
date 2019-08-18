package ir.farzinnasiri.Server.gameObject.extras;

import ir.farzinnasiri.Server.gameObject.GameObject;
import ir.farzinnasiri.Utils.Assets;
import ir.farzinnasiri.Utils.UniqueId;
import org.json.JSONObject;

import java.awt.*;

public class Food extends GameObject {

    private int level;
    private int points;
    private String food;

    public Food() {
        super(UniqueId.getIdentifier());

    }

    public void init(int level, double x, double y) {
        this.level = level;
        setX(x);
        setY(y);
        setAlive(true);
        points = 500;
        creatFood();
    }

    private void creatFood() {
        points = points * level;

        switch (level) {
            case 1:
                food = ("LEG");
                break;
            case 2:
                food = ("BURGER");
                break;
            case 3:
                food = ("DOUBLE_BURGER");
                break;
        }
    }


    @Override
    public void update(double elapsed) {
        if (getY() < 750) {
            setY(getY() + 1 * elapsed);
        } else {
            setAlive(false);
        }

    }


    @Override
    public Rectangle getRect() {
        int width = Assets.getInstance().getExtras(food).getWidth();
        int height = Assets.getInstance().getExtras(food).getHeight();
        return new Rectangle((int) getX() + 10, (int) getY() + 5, width - 10, height - 10);
    }

    @Override
    public void destroy() {

    }

    @Override
    protected void isOutOfBounds() {

    }

    public int getPoints() {
        return points;
    }


    @Override
    public String toString() {
        JSONObject extra = new JSONObject();
        extra.put("id", getId());
        extra.put("x", getX());
        extra.put("y", getY());
        extra.put("object", "FOOD");
        extra.put("kind", food);

        return extra.toString();

    }
}
