package ir.farzinnasiri.Client.views;

import ir.farzinnasiri.Utils.Assets;
import ir.farzinnasiri.Utils.Timer;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class DrawableObject {
    private int id;
    private int x, y;
    private BufferedImage[] images;
    private String object;
    private Timer timer;
    private int index;
    private int width, height;
    private double rotation;


    private boolean inUse;
    private int timerStep;
    private AffineTransform at;
    private boolean rePlay;


    private boolean finished;

    public DrawableObject() {


    }


    public DrawableObject(int id, String object, String kind, int x, int y) {
        this(id, object, kind, x, y, 0);


    }

    public DrawableObject(int id, String object, String kind, int x, int y, double rotation) {
        this(id, object, kind, x, y, -1, -1, rotation);

    }

    public DrawableObject(int id, String object, String kind, int x, int y, int width,
                          int height, double rotation) {

        init(id, object, kind, x, y, width, height, rotation);

    }

    public void init(int id, String object, String kind, int x, int y, int width,
                     int height, double rotation) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.object = object;
        this.height = height;
        this.width = width;
        this.rotation = rotation;
        inUse = true;
        finished = false;
        timerStep = 25;
        timer = new Timer();
        index = 0;

        resolveObject(object, kind);

    }


    private void rotate() {
        at = AffineTransform.getTranslateInstance(x - width / 2, y);
        at.rotate(rotation, width / 2, 0);

    }

    public void draw(Graphics2D g2d) {

        if (images != null) {
            if (images.length == 1) {
                if ((height == -1) && (width == -1)) {
                    g2d.drawImage(images[0], x, y, null);

                } else if (rotation != 0) {
                    rotate();
                    g2d.drawImage(images[0], at, null);
                } else {
                    g2d.drawImage(images[0], x, y, width, height, null);
                }


            } else {
                g2d.drawImage(images[index], x, y, null);
                if (timer.timeEvent(timerStep)) {
                    if (rePlay) {
                        if (index == images.length - 1) {
                            index = 0;
                        }
                    }
                    if (index < images.length - 1) {
                        index++;

                    } else {
                        finished = true;
                    }
                }

            }
        }


    }


    private void resolveObject(String object, String kind) {

        if (object.equals("SPACESHIP")) {
            images = new BufferedImage[1];
            images[0] = Assets.getInstance().getSpaceShip(kind);
        } else if (object.equals("ENEMY")) {
            rePlay = true;
            images = Assets.getInstance().getEnemy(kind);
            setTimerStep(40);
        } else if (object.equals("LASER")) {
            images = new BufferedImage[1];
            images[0] = Assets.getInstance().getWeapon(kind + "LASER");
        } else if (object.equals("EXHAUST")) {
            rePlay = true;
            images = Assets.getInstance().getExhausts(kind);
        } else if (object.equals("MISSILE")) {
            images = new BufferedImage[1];
            images[0] = Assets.getInstance().getWeapon("MISSILE");
        } else if (object.equals("EXPLOSION")) {
            rePlay = false;
            images = Assets.getInstance().getEffect("MISSILE_EXPLOSION");

        } else if (object.equals("EXTRA")) {
            rePlay = false;
            images = new BufferedImage[1];
            images[0] = Assets.getInstance().getExtras(kind);
        }
    }

    public BufferedImage[] getImages() {
        return images;
    }

    public int getId() {
        return id;
    }


    public String getObject() {
        return object;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public boolean isInUse() {
        return inUse;
    }

    public void setInUse(boolean inUse) {
        this.inUse = inUse;
    }

    public void setTimerStep(int timerStep) {
        this.timerStep = timerStep;
    }

    public boolean isFinished() {
        return finished;
    }
    public void setRotation(double rotation) {
        this.rotation = rotation;
    }


}
