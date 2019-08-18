package ir.farzinnasiri.Server.gameObject.enemies;

public abstract class Group {
    private int id;
    private int difficultyLevel;
    private boolean positioning;
    private boolean spanned;
    private boolean allDead;

    public boolean isAllDead() {
        return allDead;
    }

    public void setAllDead(boolean allDead) {
        this.allDead = allDead;
    }

    public boolean isSpanned() {
        return spanned;
    }

    public void setSpanned(boolean spanned) {
        this.spanned = spanned;
    }

    public int getDifficultyLevel() {
        return difficultyLevel;
    }

    public void setDifficultyLevel(int difficultyLevel) {
        this.difficultyLevel = difficultyLevel;
    }

    public boolean isPositioning() {
        return positioning;
    }

    public void setPositioning(boolean positioning) {
        this.positioning = positioning;
    }

    public  Group(int id, int difficultyLevel){
        this.id = id;
        this.difficultyLevel = difficultyLevel;

    }


    public abstract void update(double elapsed);

    public abstract void spanGroup();
    public abstract void creatInvaders();

    public abstract boolean allDead();


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
