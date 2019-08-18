package ir.farzinnasiri.Server.gameObject.enemies.groups;

import ir.farzinnasiri.Utils.Vector2D;

public class RotationalGroup extends CircularGroup {

    public RotationalGroup(int difficulty) {
        super(difficulty);

        setCenter(new Vector2D(700, 300));
        setAngularVelocity(0.6);
        setRandomPosition(false);
        setComeFromOutside(true);

    }

    @Override
    public String toString() {
        return "RotationalGroup";
    }







}
