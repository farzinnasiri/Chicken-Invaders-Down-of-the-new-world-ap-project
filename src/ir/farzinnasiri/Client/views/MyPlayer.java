package ir.farzinnasiri.Client.views;

import ir.farzinnasiri.Client.ClientConnectionHandler;
import ir.farzinnasiri.Client.systemManager.PlayersManager;
import ir.farzinnasiri.Utils.Constants;
import ir.farzinnasiri.Utils.PlayerProperties;
import org.json.JSONObject;

import java.awt.event.*;

import ir.farzinnasiri.Utils.Timer;


public class MyPlayer extends PlayerView implements KeyListener, MouseMotionListener, MouseListener {

    private int x, y;
    private int width, height;
    private boolean left, right, up, down;
    private final double speed = 5.0d;

    private boolean overheated;
    private boolean dead;


    private boolean exploding;
    private ClientConnectionHandler connection;
    private int id;
    private int maxHeat;
    private int heat;

    private int fireLevel;
    private int score;
    private int food;
    private int life;
    private int missile;

    private long durationInGame;



    private String weaponType;



    public MyPlayer(int id, String name, String kind, int x, int y, int maxHeat, String exhaustColor) {
        super(id, name, kind, x, y, exhaustColor);
        this.id = id;
        this.x = x;
        this.y = y;
        this.width = Constants.SPACESHIP_WIDTH;
        this.height = Constants.SPACESHIP_HEIGHT;
        this.maxHeat = maxHeat;



    }

    public void update(double elapsed) {
        if (!dead && !isLost()) {
            if (right && !left && x < Constants.WIDTH - 10 - width) {
                x += speed * elapsed;
            }
            if (!right && left && x > 10) {
                x -= speed * elapsed;
            }
            if (!up && down && y < Constants.HEIGHT - 25 - height) {
                y += speed * elapsed;
            }
            if (up && !down && y > 50) {
                y -= speed * elapsed;
            }
            sendCoordinates();
        }else{
            x= (Constants.WIDTH - 70) / 2;
            y= Constants.HEIGHT - 110;
        }
    }


    public boolean isAlive() {
        return !dead;
    }



    public void setDead(boolean dead) {
        this.dead = dead;
    }

    public void setOverheated(boolean overheated) {
        this.overheated = overheated;
    }


    private synchronized void sendCoordinates() {

        JSONObject coordinates = new JSONObject();
        coordinates.put("id", id);
        coordinates.put("x", this.x);
        coordinates.put("y",this.y);

        connection.sendCommend("PLAYER_COORDINATE:" + coordinates.toString());

    }


    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (!dead && !isLost()) {
            int key = e.getKeyCode();
            //handling movement
            if (key == KeyEvent.VK_D || key == KeyEvent.VK_RIGHT) {
                right = true;

            } else if (key == KeyEvent.VK_A || key == KeyEvent.VK_LEFT) {
                left = true;

            } else if (key == KeyEvent.VK_W || key == KeyEvent.VK_UP) {
                up = true;

            } else if (key == KeyEvent.VK_S || key == KeyEvent.VK_DOWN) {
                down = true;

            }
            //handling shooting
            if (key == KeyEvent.VK_SPACE && !overheated) {
                shootLaser(true);

            } else if (key == KeyEvent.VK_M && !overheated) {
                shootMissile(true);

            }

            if (key == KeyEvent.VK_G) {
                connection.sendCommend("START");

            }
        }


    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (!dead && !isLost()) {
            int key = e.getKeyCode();
            if (key == KeyEvent.VK_D || key == KeyEvent.VK_RIGHT) {
                right = false;

            } else if (key == KeyEvent.VK_A || key == KeyEvent.VK_LEFT) {
                left = false;


            } else if (key == KeyEvent.VK_W || key == KeyEvent.VK_UP) {
                up = false;

            } else if (key == KeyEvent.VK_S || key == KeyEvent.VK_DOWN) {
                down = false;

            }
            if (key == KeyEvent.VK_SPACE) {
                shootLaser(false);

            } else if (key == KeyEvent.VK_M) {
                shootMissile(false);

            }
        }

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        movePlayer(e);


    }

    @Override
    public void mouseMoved(MouseEvent e) {
        movePlayer(e);


    }

    private void movePlayer(MouseEvent e) {
        if (!dead && !isLost()) {
            int X = e.getX();
            int Y = e.getY();
            if (X < Constants.WIDTH - 10 - width && X > 10) {
                this.x = X;
                sendCoordinates();
            }
            if (Y < Constants.HEIGHT - 15 - height && Y > 10) {
                this.y = Y;
                sendCoordinates();
            }
        }
    }

    private void shootLaser(boolean shooting) {
        JSONObject shoot = new JSONObject();
        shoot.put("laser", shooting);
        connection.sendCommend("SHOOT:" + shoot.toString());

    }

    private void shootMissile(boolean shooting) {
        JSONObject shoot = new JSONObject();
        shoot.put("missile", shooting);
        connection.sendCommend("SHOOT:" + shoot.toString());


    }
    public int getFireLevel() {
        return fireLevel;
    }

    public void setFireLevel(int fireLevel) {
        this.fireLevel = fireLevel;
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
    }

    public int getLife() {
        return life;
    }

    public void setLife(int life) {
        this.life = life;
    }

    public String getWeaponType() {
        return weaponType;
    }

    public void setWeaponType(String weaponType) {
        this.weaponType = weaponType;
    }

    public int getMissile() {
        return missile;
    }

    public void setMissile(int missile) {
        this.missile = missile;
    }


    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1 && !overheated) {
            shootLaser(true);


        }
        if (e.getButton() == MouseEvent.BUTTON3) {
            shootMissile(true);

        }

    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            shootLaser(false);

        }
        if (e.getButton() == MouseEvent.BUTTON3) {
            shootMissile(false);
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }


    public void setConnection(ClientConnectionHandler clientConnectionHandler) {

        connection = clientConnectionHandler;
    }

    public void setMaxHeat(Integer maxHeat) {
        this.maxHeat = maxHeat;
    }

    public void setHeat(Integer heat) {
        this.heat = heat;
    }

    public int getHeat() {
        return heat;
    }

    public int getMxHeat() {
        return maxHeat;
    }

    public boolean isOverheated() {
        return overheated;
    }
    public long getDurationInGame() {
        return durationInGame;
    }

    public void setDurationInGame(long durationInGame) {
        this.durationInGame = durationInGame;
    }

    public void saveProperties(int wave) {
        PlayerProperties playerProperties = PlayersManager.getInstance().getPlayerProperties(getName());
        playerProperties.setFood(food);
        playerProperties.setMissile(missile);
        playerProperties.setFireLevel(fireLevel);
        playerProperties.setLife(life);
        playerProperties.setWavesPassed(wave);
        playerProperties.setDuration(durationInGame);
        playerProperties.setWeaponType(weaponType);
        playerProperties.setScore(score);

        PlayersManager.getInstance().savePlayerProperties(playerProperties);

    }
}