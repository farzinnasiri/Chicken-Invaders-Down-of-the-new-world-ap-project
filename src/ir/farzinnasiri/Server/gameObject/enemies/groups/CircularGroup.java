package ir.farzinnasiri.Server.gameObject.enemies.groups;

import ir.farzinnasiri.Server.GameState;
import ir.farzinnasiri.Server.gameObject.enemies.Group;
import ir.farzinnasiri.Server.gameObject.enemies.chickens.Chicken;
import ir.farzinnasiri.Utils.Constants;
import ir.farzinnasiri.Utils.Timer;
import ir.farzinnasiri.Utils.UniqueId;
import ir.farzinnasiri.Utils.Vector2D;

import java.util.*;

public class CircularGroup extends Group {

    private List<List<Chicken>> chickensInCircles;
    private int initialRadius;
    private double initialDegree;
    private Timer randomPositionTimer;
    private String[] types;

    private double angularVelocity;

    private Vector2D center;

    private GameState gameState;
    private boolean randomPosition;
    private boolean comeFromOutside;


    public CircularGroup(int difficulty) {
        super(UniqueId.getIdentifier(), difficulty);

        chickensInCircles = Collections.synchronizedList(new ArrayList<>());
        initialRadius = 100;
        initialDegree = 0;
        angularVelocity = 0.2;
        center = new Vector2D(0, 0);
        Random r = new Random(System.currentTimeMillis());
        center.setX(-r.nextInt(Constants.WIDTH / 3));
        center.setY(r.nextInt(Constants.HEIGHT));

        types = new String[]{"BLUE_CHICKENS", "PINK_CHICKENS"
                , "GREEN_CHICKENS", "YELLOW_CHICKENS"};

        randomPosition = true;
        randomPositionTimer = new Timer();
        gameState = GameState.getInstance();


    }

    @Override
    public void update(double elapsed) {
        setAllDead(allDead());
        updateChickens();
        if (randomPosition && randomPositionTimer.timeEvent(15000)) {
            Random r = new Random(System.currentTimeMillis());
            center.setX(r.nextInt(Constants.WIDTH));
            center.setY(r.nextInt(Constants.HEIGHT));
        }
        initialDegree += angularVelocity;
        if (initialDegree > 360) {
            initialDegree -= 360;
        }
        double radius = initialRadius + 30;
        for (List<Chicken> chickens : chickensInCircles) {
            for (int i = 0; i < chickens.size(); i++) {
                if (chickens.get(i).isAlive()) {

                    double degree = initialDegree + (360 / chickens.size()) * i;
                    int x = (int) (center.getX() + radius * Math.cos(Math.toRadians(degree)));
                    int y = (int) (center.getY() - radius * Math.sin(Math.toRadians(degree)));

                    chickens.get(i).setNextPoint(x, y, 2);

                    chickens.get(i).update(elapsed);

                }
            }
            radius += 70;

        }

    }

    private void updateChickens() {
        for (List<Chicken> chickensInCircle : chickensInCircles) {
            Iterator<Chicken> chickenIterator = chickensInCircle.iterator();
            while (chickenIterator.hasNext()) {
                Chicken chicken = chickenIterator.next();
                if (!chicken.isAlive()) {
                    chickenIterator.remove();
                }
            }
        }
    }


    @Override
    public void spanGroup() {
        switch (getDifficultyLevel()) {
            case 1:
                chickensInCircles.add(Collections.synchronizedList(new ArrayList<>()));
                break;
            case 2:
                chickensInCircles.add(Collections.synchronizedList(new ArrayList<>()));
                chickensInCircles.add(Collections.synchronizedList(new ArrayList<>()));
                break;
            case 3:
                chickensInCircles.add(Collections.synchronizedList(new ArrayList<>()));
                chickensInCircles.add(Collections.synchronizedList(new ArrayList<>()));
                chickensInCircles.add(Collections.synchronizedList(new ArrayList<>()));
                break;
            case 4:
                chickensInCircles.add(Collections.synchronizedList(new ArrayList<>()));
                chickensInCircles.add(Collections.synchronizedList(new ArrayList<>()));
                chickensInCircles.add(Collections.synchronizedList(new ArrayList<>()));
                chickensInCircles.add(Collections.synchronizedList(new ArrayList<>()));
                break;
        }
        creatInvaders();
        setSpanned(true);
        randomPositionTimer.resetTimer();

    }

    @Override
    public void creatInvaders() {
        Random random = new Random(System.currentTimeMillis());
        int j = 1;
        for (int k = 0; k < chickensInCircles.size(); k++) {
            for (int i = 0; i < 10 * j; i++) {
                double x;
                double y;
                if (comeFromOutside) {
                    x = random.nextInt(Constants.WIDTH);

                    if (random.nextInt(2) == 0) {
                        y = Constants.HEIGHT + random.nextInt(100);
                    } else {
                        y = -random.nextInt(100);
                    }

                } else {

                    double degree = initialDegree + (360 / 10) * i;
                    x = (center.getX() + initialRadius * Math.cos(Math.toRadians(degree)));
                    y = (center.getY() - initialRadius * Math.sin(Math.toRadians(degree)));

                }
                chickensInCircles.get(k).add(gameState.addChicken(getId(), types[k], x, y));
            }

            j += 0.5;

        }

    }


    @Override
    public boolean allDead() {
        synchronized (chickensInCircles) {
            for (List<Chicken> chickensInCircle : chickensInCircles) {
                synchronized (chickensInCircle) {
                    Iterator<Chicken> chickenIterator = chickensInCircle.iterator();
                    while (chickenIterator.hasNext()){
                        if(chickenIterator.next().isAlive()){
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }





    public void setAngularVelocity(double angularVelocity) {
        this.angularVelocity = angularVelocity;
    }

    public void setRandomPosition(boolean randomPosition) {
        this.randomPosition = randomPosition;
    }

    public void setCenter(Vector2D center) {
        this.center = center;
    }

    public void setComeFromOutside(boolean comeFromOutside) {
        this.comeFromOutside = comeFromOutside;
    }
    @Override
    public String toString() {
        return "CircularGroup";
    }
}
