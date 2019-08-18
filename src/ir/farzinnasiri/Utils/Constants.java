package ir.farzinnasiri.Utils;

public class Constants {

    //display size

    public static final int WIDTH = 1500;
    public static final int HEIGHT = 800;

    public static final int BIG_BUTTON_WIDTH = Assets.getInstance().getButton("CREDITS_IN").getWidth();
    public static final int BIG_BUTTON_HEIGHT = Assets.getInstance().getButton("CREDITS_IN").getHeight();

    public static final int SPACESHIP_WIDTH = Assets.getInstance().getSpaceShip("blue").getWidth();
    public static final int SPACESHIP_HEIGHT = Assets.getInstance().getSpaceShip("blue").getHeight();

    public static final int SMALL_BUTTON_WIDTH = Assets.getInstance().getButton("BACK_IN").getWidth();
    public static final int SMALL_BUTTON_HEIGHT = Assets.getInstance().getButton("BACK_IN").getHeight();

    public static final int CHICKEN_WIDTH = Assets.getInstance().getEnemy("BLUE_CHICKENS")[0].getWidth();
    public static final int CHICKEN_HEIGHT = Assets.getInstance().getEnemy("BLUE_CHICKENS")[0].getHeight();

    public static final  int BOSS_WIDTH = Assets.getInstance().getEnemy("BOSS2")[0].getWidth();
    public static final  int BOSS_HEIGHT = Assets.getInstance().getEnemy("BOSS2")[0].getHeight();

    public static final int SERVER_FULL_ERROR = -2;
    public static final String SERVER_FULL_PROMPT = "Server can not add a new player to the game";

//    public static final int BOSS_WIDTH = Assets.getInstance().getEnemy("BOSS2")[0].getWidth();
//    public static final int BOSS_HEIGHT = Assets.getInstance().getEnemy("BOSS2")[0].getHeight();


}
