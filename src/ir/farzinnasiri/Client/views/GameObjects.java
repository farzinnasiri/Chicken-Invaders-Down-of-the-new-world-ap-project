package ir.farzinnasiri.Client.views;


import ir.farzinnasiri.Client.animations.Message;
import ir.farzinnasiri.Utils.Constants;
import ir.farzinnasiri.Utils.UniqueId;
import ir.farzinnasiri.Utils.Vector2D;

import java.awt.*;
import java.util.*;
import java.util.List;

public class GameObjects {

    private static GameObjects gameObjects;

    private HashMap<Integer, PlayerView> players;

    private List<Message> messages;

    private boolean finished;

    private List<DrawableObject> weapons;
    private List<DrawableObject> screenExtraObjects;


    private List<DrawableObject> explosions;
    private List<DrawableObject> enemies;


    private DrawableObjectPool extrasDrawableObjectPool;
    private DrawableObjectPool weaponsDrawableObjectPool;


    private DrawableObjectPool enemiesDrawableObjectPool;

    private DrawableObjectPool explosionsDrawableObjectPool;


    private boolean playing;


    private GameObjects() {
        init();


    }

    public void init() {
        players = new HashMap<>();
        messages = Collections.synchronizedList(new ArrayList<>());
        weapons = Collections.synchronizedList(new ArrayList<>());
        screenExtraObjects = Collections.synchronizedList(new ArrayList<>());
        explosions = Collections.synchronizedList(new ArrayList<>());
        enemies = Collections.synchronizedList(new ArrayList<>());

        extrasDrawableObjectPool = new DrawableObjectPool(screenExtraObjects);
        weaponsDrawableObjectPool = new DrawableObjectPool(weapons);
        explosionsDrawableObjectPool = new DrawableObjectPool(explosions);
        enemiesDrawableObjectPool = new DrawableObjectPool(enemies);


        playing = true;
        finished = false;


    }

    public static GameObjects getInstance() {
        if (gameObjects == null) {
            gameObjects = new GameObjects();
        }
        return gameObjects;

    }

    public HashMap<Integer, PlayerView> getPlayers() {
        return players;
    }

    public void addPlayer(int id, PlayerView player) {
        players.put(id, player);
    }

    public void removePlayer(int id) {
        synchronized (players) {
            Iterator<Integer> iterator = players.keySet().iterator();
            while (iterator.hasNext()) {
                Integer playerId = iterator.next();
                if (playerId == id) {
                    iterator.remove();
                }
            }
        }
    }


    public synchronized void removeWeapon(int id) {
        synchronized (weapons) {
            Iterator<DrawableObject> iterator = weapons.iterator();
            while (iterator.hasNext()) {
                DrawableObject weapon = iterator.next();
                if (weapon.getId() == id) {
                    weapon.setInUse(false);
                    weaponsDrawableObjectPool.checkIn(weapon);
                    iterator.remove();
                }


            }
        }
    }

    public synchronized List<DrawableObject> getWeapons() {
        return weapons;

    }

    public synchronized boolean isPlaying() {
        return playing;
    }

    public synchronized void setPlaying(boolean playing) {
        this.playing = playing;
    }

    public DrawableObjectPool getWeaponsDrawableObjectPool() {
        return weaponsDrawableObjectPool;
    }

    public void addMissileExplosion() {
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 15; j++) {
                addExplosion(j * 100, 30 + i * 100);


            }
        }


    }

    public void addExplosion(int x, int y) {
        DrawableObject explosion = explosionsDrawableObjectPool.checkOut();
        explosion.init(UniqueId.getIdentifier(), "EXPLOSION", ""
                , x, y, 100, 100, 0);
        explosion.setTimerStep(10);

    }

    public synchronized void removeExplosion() {
        synchronized (explosions) {
            Iterator<DrawableObject> explosionsIterator = explosions.iterator();
            while (explosionsIterator.hasNext()) {
                DrawableObject explosion = explosionsIterator.next();
                if (explosion.isFinished()) {
                    explosionsIterator.remove();
                    explosionsDrawableObjectPool.checkIn(explosion);
                }
            }
        }

    }

    public synchronized List<DrawableObject> getExplosions() {
        return explosions;
    }

    public List<DrawableObject> getEnemies() {
        return enemies;
    }

    public DrawableObjectPool getEnemiesDrawableObjectPool() {
        return enemiesDrawableObjectPool;
    }

    public void removeEnemy(int id) {
        synchronized (enemies) {
            Iterator<DrawableObject> enemyIterator = enemies.iterator();
            while (enemyIterator.hasNext()) {
                DrawableObject enemy = enemyIterator.next();
                if (enemy.getId() == id) {
                    enemyIterator.remove();
                    enemiesDrawableObjectPool.checkIn(enemy);

                }
            }
        }

    }

    public List<Message> getMessages() {
        return messages;
    }

    public void addMessage(String message) {
        Message text = new Message(new Vector2D(Constants.WIDTH / 2,
                Constants.HEIGHT / 2), false,
                message, Color.WHITE, true);
        messages.add(text);
    }

    public List<DrawableObject> getScreenExtraObjects() {
        return screenExtraObjects;
    }

    public DrawableObjectPool getExtrasDrawableObjectPool() {
        return extrasDrawableObjectPool;
    }


    public void removeExtra(int id) {
        synchronized (screenExtraObjects) {
            Iterator<DrawableObject> extraIterator = screenExtraObjects.iterator();
            while (extraIterator.hasNext()) {
                DrawableObject extra = extraIterator.next();
                if (extra.getId() == id) {
                    extraIterator.remove();
                    extrasDrawableObjectPool.checkIn(extra);
                }
            }
        }
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

}
