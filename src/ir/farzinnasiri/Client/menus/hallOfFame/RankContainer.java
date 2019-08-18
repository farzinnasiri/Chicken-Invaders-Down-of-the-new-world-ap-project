package ir.farzinnasiri.Client.menus.hallOfFame;

public class RankContainer {
    private int rank;

    private String name;

    private int score;
    private int wavesPassed;
    private long duration;

    public RankContainer(String name, int score, int wavesPassed, long duration) {
        this.name = name;
        this.score = score;
        this.wavesPassed = wavesPassed;
        this.duration = duration;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public int getWavesPassed() {
        return wavesPassed;
    }

    public void setWavesPassed(int wavesPassed) {
        this.wavesPassed = wavesPassed;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    @Override
    public String toString() {
        return "name: " + name + " waves: " + wavesPassed + " score: " + score + " duration: " + duration;
    }
}
