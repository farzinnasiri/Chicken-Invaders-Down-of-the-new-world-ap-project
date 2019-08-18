package ir.farzinnasiri.Server.gameObject;

import ir.farzinnasiri.Utils.UniqueId;

import java.awt.*;

public abstract class GameObject {
    private int id;
    private String object;
    private String kind;
    private double x,y;
    private int width,height;
    private boolean alive;
    private double life;

    public GameObject(int id){
        this.id = id;
    }



    public GameObject(String object, String kind, double x, double y, int width, int height,double life,int id) {
        this.object = object;
        this.kind = kind;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.id = id;
        this.life = life;
    }


    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getId(){
        return id;
    }
    public boolean isAlive(){
        return alive;
    }
    public void setAlive(boolean alive){
        this.alive = alive;
    }
    public double getLife() {
        return life;
    }

    public void setLife(double life) {
        this.life = life;
    }

    public abstract void update(double elapsed);

    public abstract Rectangle getRect();

    public abstract void destroy();

    protected abstract void isOutOfBounds();
}
