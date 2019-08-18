package ir.farzinnasiri.Server;

import ir.farzinnasiri.Server.gameObject.enemies.WavesHandler;
import ir.farzinnasiri.Server.gameObject.enemies.chickens.Boss;
import ir.farzinnasiri.Server.gameObject.enemies.chickens.Chicken;
import ir.farzinnasiri.Server.gameObject.extras.Coin;
import ir.farzinnasiri.Server.gameObject.extras.Egg;
import ir.farzinnasiri.Server.gameObject.extras.Food;
import ir.farzinnasiri.Server.gameObject.extras.PowerUp;
import ir.farzinnasiri.Server.gameObject.pools.*;
import ir.farzinnasiri.Server.gameObject.spaceship.SpaceShip;
import ir.farzinnasiri.Server.gameObject.weapons.Laser;
import ir.farzinnasiri.Server.gameObject.weapons.Missile;
import ir.farzinnasiri.Utils.Constants;
import ir.farzinnasiri.Utils.DataBase;
import ir.farzinnasiri.Utils.UniqueId;
import org.json.JSONObject;

import java.awt.*;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.*;
import java.util.List;


public class GameState {
    private static GameState gameState;

    private List<ServerClient> clientsList;
    private List<SpaceShip> spaceShips;
    private List<Chicken> chickens;
    private List<Food> foods;
    private List<Egg> eggs;
    private List<PowerUp> powerUps;
    private List<Coin> coins;


    private List<Laser> lasers;
    private List<Missile> missiles;

    private boolean playing;
    private boolean gameFinished;


    private int playersCapacity;
    private int maxWaves;


    private boolean game;


    //pools
    private LaserObjectPool laserObjectPool;
    private ChickenObjectPool chickenObjectPool;
    private EggObjectPool eggObjectPool;
    private FoodObjectPool foodObjectPool;
    private PowerUpObjectPool powerUpObjectPool;
    private CoinObjectPool coinObjectPool;

    private WavesHandler wavesHandler;
    private int startingWave;
    private Boss boss;
    private boolean multiplayer;


    public static GameState getInstance() {

        if (gameState == null) {
            gameState = new GameState();
        }

        return gameState;

    }

    public void init() {

        clientsList = Collections.synchronizedList(new ArrayList<>());
        spaceShips = Collections.synchronizedList(new ArrayList<>());
        chickens = Collections.synchronizedList(new ArrayList<>());
        lasers = Collections.synchronizedList(new ArrayList<>());
        missiles = Collections.synchronizedList(new ArrayList<>());
        eggs = Collections.synchronizedList(new ArrayList<>());
        powerUps = Collections.synchronizedList(new ArrayList<>());
        coins = Collections.synchronizedList(new ArrayList<>());
        foods = Collections.synchronizedList(new ArrayList<>());


        playing = true;
        gameFinished = false;
        game = false;


        instantiatingPools();

    }


    private void instantiatingPools() {
        laserObjectPool = new LaserObjectPool(lasers);
        chickenObjectPool = new ChickenObjectPool(chickens);
        eggObjectPool = new EggObjectPool(eggs);
        foodObjectPool = new FoodObjectPool(foods);
        powerUpObjectPool = new PowerUpObjectPool(powerUps);
        coinObjectPool = new CoinObjectPool(coins);
    }

    public void startGame() {
        if (!game) {
            game = true;
            wavesHandler = new WavesHandler(maxWaves, startingWave);
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    Thread wavesHandlerThread = new Thread(wavesHandler, "Waves Handler Thread");
                    wavesHandlerThread.start();
                }
            }, 10 * 1000);

        }
        sendMessageToAll("Game Starts soon!");

    }

    public List<ServerClient> getClientsList() {
        return clientsList;
    }

    public List<SpaceShip> getSpaceShips() {
        return spaceShips;
    }

    public synchronized void addClient(ServerClient serverClient) {
        synchronized (clientsList) {
            clientsList.add(serverClient);
        }

    }


    public synchronized void addPlayer(SpaceShip spaceShip) {
        synchronized (spaceShips) {
            if (!isGame()) {
                spaceShips.add(spaceShip);
            }
        }
        if (isFull() && !isGame()) {
            startGame();
        }

    }


    public void stopGame() {
        sendCommandToAll("GAME_FINISHED");
        if (multiplayer) {
            resolveRanks();
        }
        game = false;
    }

    private void resolveRanks() {
        synchronized (spaceShips) {
            HashMap<String, Integer> playersScores = new HashMap<>();

            for (SpaceShip spaceShip : spaceShips) {
                playersScores.put(spaceShip.getName(), spaceShip.getScore());

            }

            DataBase db = new DataBase();
            try {
                db.setConnection();
                db.creatRankingTable(playersScores);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean isGame() {
        return game;
    }


    public synchronized void update(double elapsed) {
        synchronized (spaceShips) {
            Iterator<SpaceShip> spaceShipIterator = spaceShips.iterator();
            while (spaceShipIterator.hasNext()) {
                SpaceShip spaceShip = spaceShipIterator.next();
                if (spaceShip.isAlive()) resolveCollisions(spaceShip);
                spaceShip.update(elapsed);
            }
            notify();

        }

        synchronized (missiles) {
            Iterator<Missile> missileIterator = missiles.iterator();
            while (missileIterator.hasNext()) {
                Missile missile = missileIterator.next();
                if (missile.isAlive()) {
                    missile.update(elapsed);
                } else {
                    missileIterator.remove();
                    destroyOnScreenEnemies(missile);
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("id", missile.getId());
                    jsonObject.put("explosion", missile.hasExploded());
                    sendCommandToAll("REMOVE_WEAPON:" + jsonObject.toString());
                    UniqueId.returnId(missile.getId());

                }
            }
            notify();

        }
        synchronized (lasers) {
            Iterator<Laser> laserIterator = lasers.iterator();
            while (laserIterator.hasNext()) {
                Laser laser = laserIterator.next();
                resolveCollisions(laser);
                if (laser.isAlive()) {
                    laser.update(elapsed);
                } else {
                    laserIterator.remove();

                    removeLaser(laser);
                }
            }
            notify();

        }
        if (isGame() && wavesHandler != null) wavesHandler.update(elapsed);

        synchronized (eggs) {
            Iterator<Egg> eggIterator = eggs.iterator();
            while (eggIterator.hasNext()) {
                Egg egg = eggIterator.next();
                String sound = resolveCollision(egg);
                if (!egg.isAlive()) {
                    eggIterator.remove();
                    eggObjectPool.checkIn(egg);
                    sendCommandToAll("REMOVE_EXTRA:" + egg.getId() + "," + sound);
                } else {
                    egg.update(elapsed);
                }

            }
            notify();


        }
        synchronized (powerUps) {
            Iterator<PowerUp> powerUpIterator = powerUps.iterator();
            while (powerUpIterator.hasNext()) {
                PowerUp powerUp = powerUpIterator.next();
                String sound = resolveCollision(powerUp);
                if (!powerUp.isAlive()) {
                    powerUpIterator.remove();
                    powerUpObjectPool.checkIn(powerUp);
                    sendCommandToAll("REMOVE_EXTRA:" + powerUp.getId() + "," + sound);
                } else {
                    powerUp.update(elapsed);
                }

            }
            notify();


        }
        synchronized (foods) {
            Iterator<Food> foodIterator = foods.iterator();
            while (foodIterator.hasNext()) {
                Food food = foodIterator.next();
                String sound = resolveCollision(food);
                if (!food.isAlive()) {
                    foodIterator.remove();
                    foodObjectPool.checkIn(food);
                    sendCommandToAll("REMOVE_EXTRA:" + food.getId() + "," + sound);

                } else {
                    food.update(elapsed);
                }
            }
            notify();

        }

        synchronized (coins) {
            Iterator<Coin> coinIterator = coins.iterator();
            while (coinIterator.hasNext()) {
                Coin coin = coinIterator.next();
                String sound = resolveCollision(coin);
                if (!coin.isAlive()) {
                    coinIterator.remove();
                    coinObjectPool.checkIn(coin);
                    sendCommandToAll("REMOVE_EXTRA:" + coin.getId() + "," + sound);
                } else {
                    coin.update(elapsed);
                }
            }
            notify();

        }


    }

    private void resolveCollisions(SpaceShip spaceShip) {
        synchronized (chickens) {
            if (spaceShip.isAlive() && !spaceShip.isShield()) {
                Iterator<Chicken> chickenIterator = chickens.iterator();
                while (chickenIterator.hasNext()) {
                    Chicken chicken = chickenIterator.next();
                    if (chicken.getRect().intersects(spaceShip.getRect())) {
                        chicken.kill(chicken.getLife(), spaceShip.getId());
                        spaceShip.kill();

                    }
                }
            }
        }
        if (boss != null && spaceShip.isAlive() && !spaceShip.isShield()) {
            synchronized (boss) {
                if (boss.isAlive()) {
                    if (spaceShip.getRect().intersects(boss.getRect())) {
                        spaceShip.kill();
                        boss.kill(100, spaceShip.getId());
                    }
                }
            }
        }
    }

    private String resolveCollision(PowerUp powerUp) {
        synchronized (spaceShips) {
            for (SpaceShip spaceShip : spaceShips) {
                if (spaceShip.getRect().intersects(powerUp.getRect()) && spaceShip.isAlive()) {
                    powerUp.setAlive(false);
                    spaceShip.addPower(powerUp.getPower());
                    return "POWERUP";
                }

            }
            return "NO_SOUND";
        }

    }

    private String resolveCollision(Egg egg) {
        synchronized (spaceShips) {
            for (SpaceShip spaceShip : spaceShips) {
                if (spaceShip.getRect().intersects(egg.getRect()) && spaceShip.isAlive() && !spaceShip.isShield()) {
                    egg.setAlive(false);
                    spaceShip.kill();
                    return "EXPLOSION";
                }

            }
            return "NO_SOUND";

        }
    }

    private String resolveCollision(Food food) {
        synchronized (spaceShips) {
            for (SpaceShip spaceShip : spaceShips) {
                if (spaceShip.getRect().intersects(food.getRect()) && spaceShip.isAlive()) {
                    food.setAlive(false);
                    spaceShip.setScore(spaceShip.getScore() + food.getPoints());
                    spaceShip.setFood(spaceShip.getFood() + 1);
                    return "EATING";
                }
            }
            return "NO_SOUND";
        }

    }

    private String resolveCollision(Coin coin) {
        synchronized (spaceShips) {
            for (SpaceShip spaceShip : spaceShips) {

            }
            return "NO_SOUND";
        }


    }


    public synchronized void getState(PrintWriter output) {
        synchronized (spaceShips) {
            Iterator<SpaceShip> spaceShipIterator = spaceShips.iterator();
            while (spaceShipIterator.hasNext()) {
                SpaceShip spaceShip = spaceShipIterator.next();
                if (!spaceShip.isLost()) {
                    output.println("PLAYER_STATUS:" + spaceShip.toString());
                }
            }
            notify();

        }
        synchronized (lasers) {
            Iterator<Laser> laserIterator = lasers.iterator();
            while (laserIterator.hasNext()) {
                Laser laser = laserIterator.next();
                if (laser.isAlive()) {
                    output.println("LASER:" + laser.toString());
                }
            }
            notify();

        }
        synchronized (missiles) {
            Iterator<Missile> missileIterator = missiles.iterator();
            while (missileIterator.hasNext()) {
                Missile missile = missileIterator.next();
                output.println("MISSILE:" + missile.toString());

            }
            notify();


        }

        synchronized (chickens) {
            synchronized (chickens) {
                Iterator<Chicken> chickenIterator = chickens.iterator();
                while (chickenIterator.hasNext()) {
                    Chicken chicken = chickenIterator.next();
                    output.println("CHICKEN" + chicken.toString());
                }
            }

        }
        synchronized (eggs) {
            Iterator<Egg> eggIterator = eggs.iterator();
            while (eggIterator.hasNext()) {
                Egg egg = eggIterator.next();
                if (egg.isAlive()) {
                    output.println("EXTRA:" + egg.toString());
                }

            }
            notify();


        }
        synchronized (powerUps) {
            Iterator<PowerUp> powerUpIterator = powerUps.iterator();
            while (powerUpIterator.hasNext()) {
                PowerUp powerUp = powerUpIterator.next();
                if (powerUp.isAlive()) {
                    output.println("EXTRA:" + powerUp.toString());
                }

            }
            notify();


        }
        synchronized (foods) {
            Iterator<Food> foodIterator = foods.iterator();
            while (foodIterator.hasNext()) {
                Food food = foodIterator.next();
                if (food.isAlive()) {
                    output.println("EXTRA:" + food.toString());

                }
            }
            notify();

        }

        synchronized (coins) {
            Iterator<Coin> coinIterator = coins.iterator();
            while (coinIterator.hasNext()) {
                Coin coin = coinIterator.next();
                if (coin.isAlive()) {
                    output.println("EXTRA:" + coin.toString());
                }
            }
            notify();

        }

        if (boss != null) {
            synchronized (boss) {
                if (boss.isAlive() || boss.isMoving()) {
                    output.println("BOSS:" + boss.toString());
                }
            }
        }


    }

    private void resolveCollisions(Laser laser) {
        synchronized (chickens) {
            for (Chicken chicken : chickens) {
                if (chicken.getRect().intersects(laser.getRect())) {
                    laser.setAlive(false);
                    chicken.kill(laser.getPower(), laser.getPlayerId());
                }
            }
        }
        synchronized (spaceShips) {
            for (SpaceShip spaceShip : spaceShips) {
                if (spaceShip.getRect().intersects(laser.getRect()) && spaceShip.isAlive() && !spaceShip.isShield()) {
                    laser.setAlive(false);
                    spaceShip.kill();
                }
            }
        }
        if (boss != null && laser.isAlive()) {
            synchronized (boss) {
                if (boss.isAlive()) {
                    if (laser.getRect().intersects(boss.getRect())) {
                        laser.setAlive(false);
                        sendExplosionCommand(laser.getX(), laser.getY());
                        boss.kill(laser.getPower(), laser.getPlayerId());
                    }
                }
            }
        }
        removeDeadChickens();

    }

    public synchronized void sendMessageToAll(String message) {

        synchronized (clientsList) {
            for (ServerClient serverClient : clientsList) {
                serverClient.promptMessage(message);
            }
        }
    }

    public synchronized void sendCommandToAll(String command) {
        synchronized (clientsList) {
            for (ServerClient serverClient : clientsList) {
                serverClient.sendCommand(command);
            }
        }

    }

    public synchronized void setSpaceShipShooting(int id, boolean shooting, boolean missile) {
        synchronized (spaceShips) {
            Iterator<SpaceShip> spaceShipIterator = spaceShips.iterator();
            while (spaceShipIterator.hasNext()) {
                SpaceShip spaceShip = spaceShipIterator.next();
                if (!spaceShip.isLost() && spaceShip.getId() == id) {
                    if (missile) {
                        spaceShip.shootMissile(shooting);

                    } else {
                        spaceShip.setShooting(shooting);
                    }
                }
            }
        }

    }

    public synchronized boolean isSpaceShipShooting(int id) {
        synchronized (spaceShips) {
            for (SpaceShip spaceShip : spaceShips) {
                if (spaceShip.getId() == id) {
                    return spaceShip.isShooting();
                }
            }
            return false;
        }
    }

    public synchronized void destroyOnScreenEnemies(Missile missile) {
        synchronized (missiles) {
            for (Chicken chicken : chickens) {
                if (chicken.getRect().intersects(new Rectangle(0,0, Constants.WIDTH,Constants.HEIGHT))) {
                    chicken.kill(missile.getPower(), missile.getPlayerId());
                }
                if (boss != null) {
                    synchronized (boss) {
                        if (boss.isAlive()) {
                            if (boss.getRect().intersects(chicken.getRect())) {
                                boss.kill(missile.getPower() * 10, missile.getPlayerId());
                            }
                        }

                    }
                }
            }

        }
        removeDeadChickens();
    }

    public synchronized void addMissile(int id, double x, double y) {
        synchronized (missiles) {
            missiles.add(new Missile(id, x, y));
        }
    }


    public void setPlayersCapacity(int playersCapacity) {
        this.playersCapacity = playersCapacity;
    }


    public boolean isFull() {
        synchronized (spaceShips) {
            return spaceShips.size() - playersCapacity >= 0;
        }

    }

    public synchronized boolean isPlaying() {
        return playing;
    }

    public void setPlaying(boolean playing) {
        if (playing && !this.playing) {
            for (ServerClient serverClient : clientsList) {
                if (!serverClient.isViewer() && !serverClient.isPlaying()) {
                    return;
                }
            }
            sendCommandToAll("PLAYING:" + true);

        }
        this.playing = playing;
    }

    public boolean isGameFinished() {
        return gameFinished;
    }

    public int getMaxWaves() {
        return maxWaves;
    }

    public void setWavesBounds(int startingWave, int maxWaves) {
        this.maxWaves = maxWaves;
        this.startingWave = startingWave;
    }

    public List<Chicken> getChickens() {
        return chickens;
    }


    public void sendNewPlayersIn() {
        //new players in gap time;
    }

    public void addScore(int playerId, double score) {
        synchronized (spaceShips) {
            for (SpaceShip spaceShip : spaceShips) {
                if (spaceShip.getId() == playerId) {
                    spaceShip.setScore((int) (spaceShip.getScore() + score));
                    return;
                }
            }
        }


    }


    public void addEgg(double x, double y, int speed) {
        synchronized (eggs) {

            Egg egg = eggObjectPool.checkOut();
            egg.init(x, y, speed);
        }

    }

    public void addFood(int level, double x, double y) {
        synchronized (foods) {
            Food food = foodObjectPool.checkOut();
            food.init(level, x, y);
        }

    }

    public void addPowerUp(double x, double y) {
        synchronized (powerUps) {
            PowerUp powerUp = powerUpObjectPool.checkOut();
            powerUp.init(x, y);
        }

    }

    public void addCoin() {

    }


    public void removeClient(int id) {
        synchronized (clientsList) {
            for (ServerClient serverClient : clientsList) {
                if (serverClient.getId() == id) {
                    clientsList.remove(serverClient);
                    break;
                }
            }
        }
        if (clientsList.size() == 0) {
            gameFinished = true;
        }
    }

    public void removeSpaceShip(int id) {
        synchronized (spaceShips) {
            for (SpaceShip spaceShip : spaceShips) {
                if (spaceShip.getId() == id) {
                    spaceShips.remove(spaceShip);
                    break;
                }
            }
        }
    }

    public void removeLaser(Laser laser) {

        UniqueId.returnId(laser.getId());
        laserObjectPool.checkIn(laser);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", laser.getId());
        sendCommandToAll("REMOVE_WEAPON:" + jsonObject.toString());

    }


    public synchronized void addLaser(int playerId, double x, double y, String color, double power, double degree) {
        synchronized (lasers) {
            laserObjectPool.checkOut();
            lasers.get(lasers.size() - 1).init(playerId, x, y, color, power, degree);
            notify();
        }

    }

    public synchronized Chicken addChicken(int groupId, String kind, double x, double y) {
        synchronized (chickens) {
            Chicken chicken = chickenObjectPool.checkOut();
            chicken.init(groupId, kind, x, y);

            return chicken;
        }
    }

    public synchronized void removeDeadChickens() {
        synchronized (chickens) {
            Iterator<Chicken> chickenIterator = chickens.iterator();
            while (chickenIterator.hasNext()) {
                Chicken chicken = chickenIterator.next();
                if (!chicken.isAlive()) {
                    chickenIterator.remove();
                    chickenObjectPool.checkIn(chicken);
                    sendCommandToAll("REMOVE_ENEMY:" + chicken.getId());
                }
            }
        }

    }

    public synchronized void sendExplosionCommand(double x, double y) {
        JSONObject explosionJson = new JSONObject();
        explosionJson.put("x", x);
        explosionJson.put("y", y);

        sendCommandToAll("EXPLOSION:" + explosionJson.toString());


    }

    public void setBoss(Boss boss) {
        this.boss = boss;
    }

    public void addNewGroup(Class c) {
        wavesHandler.addNewGroup(c);
        wavesHandler.addNewGroup(c);
    }

    public void setMultiplayer(boolean multiplayer) {
        this.multiplayer = multiplayer;
    }

    public boolean isMultiplayer() {
        return multiplayer;
    }
}
