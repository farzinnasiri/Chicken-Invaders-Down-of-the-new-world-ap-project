package ir.farzinnasiri.Utils;


import java.awt.image.BufferedImage;
import java.util.List;

public class PlayerProperties {

    private String name;
    private List<Integer> scores;
    private List<Integer> wavesPassed;
    private List<Long> durations;


    private int life;
    private int missile;
    private int fireLevel;
    private int food;


    private int maxHeat;



    private String spaceshipColor;
    private String engineColor;
    private String weaponType;


    public PlayerProperties(String name, List<Integer> scores, List<Integer> wavesPassed, List<Long> durations, int life,
                            int missile, int fireLevel, String weaponType, int food,int maxHeat, String spaceshipColor,
                            String engineColor) {
        this.name = name;
        this.scores = scores;
        this.wavesPassed = wavesPassed;
        this.durations = durations;
        this.life = life;
        this.missile = missile;
        this.fireLevel = fireLevel;
        this.food = food;
        this.maxHeat = maxHeat;
        this.spaceshipColor = spaceshipColor;
        this.engineColor = engineColor;
        this.weaponType = weaponType;


    }


    public String getName() {
        return name;
    }

    public int getFood() {
        return food;
    }

    public void addFood(int food) {
        this.food += food;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Integer> getScores() {
        return scores;
    }

    public List<Long> getDurations() {
        return durations;
    }

    public List<Integer> getWavesPassed() {
        return wavesPassed;
    }

    public void setScore(int score) {
        int i = scores.size() - 1;
        scores.set(i, score);

    }

    public void newGame() {
        scores.add(0);
        wavesPassed.add(0);
        durations.add((long) 0);
        setWeaponType("RED");
        setLife(5);
        setFireLevel(0);
        setMissile(3);
        setFood(0);
    }

    public void setDuration(long duration) {
        int i = durations.size() - 1;
        durations.set(i, duration);
    }

    public void setWavesPassed(int wavePassed) {
        int i = wavesPassed.size() - 1;
        wavesPassed.set(i, wavePassed);
    }


    public int getLife() {
        return life;
    }

    public void setLife(int life) {
        this.life = life;
    }

    public int getMissile() {
        return missile;
    }

    public void setMissile(int missile) {
        this.missile = missile;
    }

    public int getFireLevel() {
        return fireLevel;
    }

    public void setFireLevel(int fireLevel) {
        this.fireLevel = fireLevel;
    }

    public BufferedImage getSpaceship() {
        Assets assets = Assets.getInstance();
        return assets.getSpaceShip(spaceshipColor);
    }

    public void setSpaceshipColor(String spaceshipColor) {
        this.spaceshipColor = spaceshipColor;
    }

    public BufferedImage[] getEngine() {
        return Assets.getInstance().getExhausts(engineColor);
    }

    public void setEngineColor(String engineColor) {
        this.engineColor = engineColor;
    }

    public String getSpaceshipColor() {
        return spaceshipColor;
    }

    public String getEngineColor() {
        return engineColor;
    }

    public String getWeaponType() {
        return weaponType;
    }

    public void setWeaponType(String weaponType) {
        this.weaponType = weaponType;
    }

    public int getMaxHeat() {
        return maxHeat;
    }

    public void setMaxHeat(int maxHeat) {
        this.maxHeat = maxHeat;
    }

    public void setFood(int food) {
        this.food = food;
    }

}
