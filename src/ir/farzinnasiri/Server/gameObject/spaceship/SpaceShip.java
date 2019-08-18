package ir.farzinnasiri.Server.gameObject.spaceship;

import ir.farzinnasiri.Server.GameState;
import ir.farzinnasiri.Server.gameObject.GameObject;
import ir.farzinnasiri.Utils.Constants;
import ir.farzinnasiri.Utils.Timer;
import org.json.JSONObject;

import java.awt.*;

public class SpaceShip extends GameObject {
    private String name;



    private String exhaustColor;

    private boolean shield;
    private boolean shooting;
    private boolean lost;
    private boolean overheated;

    private int maxHeat;
    private int fireLevel;
    private int score;
    private int food;
    private int missile;

    private String weaponType;


    private long startTime;




    private Timer shieldTimer;
    private Timer laserTimer;
    private Timer missileTimer;
    private Timer deathTimer;
    private GameState gameState;


    private HeatGage heatGage;
    private boolean shootingMissile;



    public SpaceShip(String name, String kind, double x, double y, int width, int height, int life, int id
            , int maxHeat, String exhaustColor, String weaponType, int fireLevel,int food,int missile) {
        super("SPACESHIP", kind, x, y, width, height, life, id);
        this.name = name;
        this.exhaustColor = exhaustColor;
        this.maxHeat = maxHeat;
        this.weaponType = weaponType;
        this.fireLevel = fireLevel;
        this.food = food;
        this.missile = missile;
        shield = true;
        setAlive(true);
        lost = false;

        gameState = GameState.getInstance();

        heatGage = new HeatGage(id, maxHeat);

        shieldTimer = new Timer();
        laserTimer = new Timer();
        missileTimer = new Timer();
        deathTimer = new Timer();

        startTime = System.currentTimeMillis();

    }

    public void update(double elapsed) {
        setOverheated(heatGage.isOverHeated());
        heatGage.update(elapsed);
        if (shield && shieldTimer.timeEvent(10000)) {
            shield = false;
        }
        if (!isAlive()) {
            setX((Constants.WIDTH - 70) / 2);
            setY(Constants.HEIGHT - 110);
            if (deathTimer.timeEvent(3000)) {
                setAlive(true);
            }
        }

        if (isAlive()) {
            if (shooting) {
                if (laserTimer.timeEvent(250) && !overheated) {
                    heatGage.addHeat();
                    shootLaser();

                }
            }
            if (shootingMissile && missile > 0) {
                if (missileTimer.timeEvent(1000)) {
                    missile -=1;
                    gameState.addMissile(getId(), getX(), getY());
                }

            }
        }


    }

    private void shootLaser() {
        double power;
        power = (fireLevel - 3) < 0 ? getPowerKind(weaponType) : ((fireLevel - 3) * 0.25 + 1) * getPowerKind(weaponType);
        if (fireLevel == 0) {
            gameState.addLaser(getId(), getX() + 28, getY() - 65, weaponType, power, Math.toRadians(0));

        } else if (fireLevel == 1) {
            gameState.addLaser(getId(), getX() + 13, getY() - 55, weaponType, power, Math.toRadians(0));
            gameState.addLaser(getId(), getX() + 47, getY() - 55, weaponType, power, Math.toRadians(0));

        } else if (fireLevel == 2) {
            gameState.addLaser(getId(), getX() + 28, getY() - 65, weaponType, power, Math.toRadians(0));
            gameState.addLaser(getId(), getX() + 8, getY() - 45, weaponType, power, Math.toRadians(-5));
            gameState.addLaser(getId(), getX() + 58, getY() - 45, weaponType, power, Math.toRadians(5));


        } else if (fireLevel >= 3) {
            gameState.addLaser(getId(), getX() + 20, getY() - 65, weaponType, power, Math.toRadians(0));
            gameState.addLaser(getId(), getX() + 45, getY() - 65, weaponType, power, Math.toRadians(0));
            gameState.addLaser(getId(), getX() + 10, getY() - 30, weaponType, power, Math.toRadians(-5));
            gameState.addLaser(getId(), getX() + 60, getY() - 30, weaponType, power, Math.toRadians(5));
        }
    }

    private double getPowerKind(String color) {
        switch (color) {
            case "RED":
                return 1;
            case "GREEN":
                return 2;
            case "BLUE":
                return 3;

        }
        return 0;
    }

    @Override
    public Rectangle getRect() {
        return new Rectangle((int) getX() + 10, (int) getY() + 10, getWidth() - 10, getHeight() - 10);
    }

    @Override
    public void destroy() {

    }

    @Override
    protected void isOutOfBounds() {

    }

    public void kill() {
        setAlive(false);
        if(fireLevel > 0){
            fireLevel--;
        }
        gameState.sendExplosionCommand(getX(),getY());
        deathTimer.resetTimer();
        setX((Constants.WIDTH - 70) / 2);
        setY(Constants.HEIGHT - 110);
        setLife(getLife() - 1);
        if (getLife() == 0) {
            lost = true;
            gameState.sendCommandToAll("PLAYER_LOST:"+getId());
            if(!gameState.isMultiplayer()){
                gameState.sendCommandToAll("GAME_FINISHED");
            }

        } else {
            shield = true;
            shieldTimer.resetTimer();
        }
    }


    @Override
    public String toString() {
        JSONObject spaceship = new JSONObject();
        spaceship.put("name", name);
        spaceship.put("id", getId());
        spaceship.put("object", getObject());
        spaceship.put("kind", getKind());
        spaceship.put("x", getX());
        spaceship.put("y", getY());
        spaceship.put("shield", shield);
        spaceship.put("alive", isAlive());
        spaceship.put("overheat", isOverheated());
        spaceship.put("maxHeat", maxHeat);
        spaceship.put("heat", heatGage.getTemperature());
        spaceship.put("score",getScore());
        spaceship.put("life",getLife());
        spaceship.put("weaponType",weaponType);
        spaceship.put("missile",missile);
        spaceship.put("food",food);
        spaceship.put("fireLevel",fireLevel);
        spaceship.put("duration",getDurationInGame());
        spaceship.put("exhaustColor", exhaustColor);

        return spaceship.toString();


    }

    public boolean isLost() {
        return lost;
    }

    public boolean isShield() {
        return shield;
    }

    public void setShield(boolean shield) {
        this.shield = shield;
    }

    public boolean isShooting() {
        return shooting;
    }

    public void setShooting(boolean shooting) {
        this.shooting = shooting;
    }

    public void shootMissile(boolean shootingMissile) {
        this.shootingMissile = shootingMissile;
    }

    public boolean isOverheated() {
        return overheated;
    }

    public void setOverheated(boolean overheated) {
        this.overheated = overheated;
    }

    public int getFireLevel() {
        return fireLevel;
    }

    public void setFireLevel(int fireLevel) {
        this.fireLevel = fireLevel;
    }

    public void addPower(String weaponType) {
        if (weaponType.equals("GOLD")) {
            heatGage.increasMax(5);
        } else if (this.weaponType.equals(weaponType)) {
            fireLevel++;
        } else {
            this.weaponType = weaponType;

        }

    }

    public String getWeaponType() {
        return weaponType;
    }

    public void setWeaponType(String weaponType) {
        this.weaponType = weaponType;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getFood() {
        return food;
    }

    public void setFood(int food) {
        this.food = food;
        if(food == 20){
            food = 0;
            fireLevel++;
        }
    }


    public long getDurationInGame() {
        return  ((System.currentTimeMillis()-startTime)/1000);
    }

    public void setStartTime(long startTime){
        this.startTime += startTime;
    }
    public String getName() {
        return name;
    }



}
