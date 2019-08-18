package ir.farzinnasiri.Server.gameObject.enemies.groups;

import ir.farzinnasiri.Server.GameState;
import ir.farzinnasiri.Server.gameObject.enemies.Group;
import ir.farzinnasiri.Server.gameObject.enemies.chickens.Boss;
import ir.farzinnasiri.Utils.Constants;
import ir.farzinnasiri.Utils.Timer;
import ir.farzinnasiri.Utils.UniqueId;

import java.util.Random;

public class BossGroup extends Group {

    private Boss boss;
    private GameState gameState;
    private Timer shootingTimer;

    private boolean finished;


    public BossGroup(int difficultyLevel) {
        super(UniqueId.getIdentifier(), difficultyLevel);

        boss = new Boss(difficultyLevel);

        gameState = GameState.getInstance();

        shootingTimer = new Timer();

        finished = false;

    }

    @Override
    public void update(double elapsed) {


        if (shootingTimer.timeEvent(500) && boss.isAlive() && !boss.isMoving()) {
            Random random = new Random(System.currentTimeMillis());

            for (int i = 0; i < 8; i++) {
                if (random.nextInt(4) == 0) {
                    double degree = Math.toRadians((i + 1) * 45);
                    gameState.addLaser(getId(), boss.getX() + Constants.BOSS_WIDTH / 2+150*
                            Math.sin(degree),
                            boss.getY() + Constants.BOSS_HEIGHT / 2-150*
                                    Math.cos(degree), "GREEN", 1,
                            degree);
                }
            }

        } else if (!boss.isAlive()) {
            boss.setNextPoint((Constants.WIDTH - Constants.BOSS_WIDTH) / 2, -300, 1);
            if(boss.getY()+Constants.BOSS_HEIGHT <= 0){
                gameState.sendCommandToAll("REMOVE_ENEMY:"+boss.getId());
                boss.setMoving(false);
                finished = true;
                return;
            }
        }
        boss.update(elapsed);


    }

    @Override
    public void spanGroup() {
        shootingTimer.resetTimer();
        gameState.setBoss(boss);
        boss.setNextPoint((Constants.WIDTH - Constants.BOSS_WIDTH) / 2, 200, 1);
        setSpanned(true);

    }

    @Override
    public void creatInvaders() {

    }

    @Override
    public boolean allDead() {
        return finished;
    }

    @Override
    public String toString() {
        return "BossGroup";
    }
}
