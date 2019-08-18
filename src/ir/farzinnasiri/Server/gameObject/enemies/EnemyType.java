package ir.farzinnasiri.Server.gameObject.enemies;


public interface EnemyType {
    public void shoot();

    public void kill(double weaponPower, int playerId);

    public void setNextPoint(double x, double y, double speed);
}
