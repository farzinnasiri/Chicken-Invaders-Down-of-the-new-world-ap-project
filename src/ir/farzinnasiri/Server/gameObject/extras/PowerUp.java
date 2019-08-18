package ir.farzinnasiri.Server.gameObject.extras;

import ir.farzinnasiri.Server.gameObject.GameObject;
import ir.farzinnasiri.Utils.UniqueId;
import org.json.JSONObject;

import java.awt.*;
import java.util.Random;

public class PowerUp extends GameObject {
    private Random kind;
    private String color;


    public PowerUp(){
        super(UniqueId.getIdentifier());
    }

    public void init(double x, double y) {
        setX(x);
        setY(y);
        setAlive(true);
        kind = new Random();

        creatPowerUp();
    }


    private void creatPowerUp(){
        switch (kind.nextInt(2)){
            case 0:
                color = ("GOLD");
                break;
            case 1:
                switch (kind.nextInt(3)){
                    case 0:
                        color =("RED");
                        break;
                    case 1:
                        color =("GREEN");
                        break;
                    case 2:
                        color=("BLUE");
                        break;
                }
        }


    }


    @Override
    public void update(double elapsed) {
        if (getY() < 750) {
            setY(getY()+1*elapsed);
        }else {
            setAlive(false);
        }

    }



    @Override
    public void destroy() {

    }

    @Override
    protected void isOutOfBounds() {

    }

    @Override
    public Rectangle getRect(){
        return new Rectangle((int)getX()+10,(int)getY(),41,49);
    }

    public String getPower() {
        return color;
    }


    @Override
    public String toString() {
        JSONObject extra = new JSONObject();
        extra.put("id", getId());
        extra.put("x", getX());
        extra.put("y", getY());
        extra.put("object", "POWER_UP");
        extra.put("kind",  color);
        return extra.toString();

    }
}
