package ir.farzinnasiri.Client.menus.hallOfFame;


import ir.farzinnasiri.Client.buttons.Action;
import ir.farzinnasiri.Client.buttons.BackButton;
import ir.farzinnasiri.Client.buttons.Button;
import ir.farzinnasiri.Client.input.MouseInput;
import ir.farzinnasiri.Client.stateMachine.StateMachine;
import ir.farzinnasiri.Client.stateMachine.SuperStateMachine;
import ir.farzinnasiri.Utils.*;
import ir.farzinnasiri.Client.systemManager.PlayersManager;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class
HallOfFame extends SuperStateMachine {

    private BufferedImage highscoreBox;
    private MouseInput mouseInput;
    private ArrayList<Button> buttons;
    private List<RankContainer> allPlayersGames;
    private Assets assets;

    public HallOfFame(StateMachine stateMachine) {
        super(stateMachine);
        assets = Assets.getInstance();
        highscoreBox = assets.getBox("HIGHSCORE");
        mouseInput = new MouseInput();
        buttons = new ArrayList<>();
        allPlayersGames = new ArrayList<>();
        setButtons();
        getPlayers();
        sort();

    }


    private void setButtons() {
        buttons.add(new Button(assets.getButton("RESET_OUT"), assets.getButton("RESET_IN"),
                Constants.WIDTH - Constants.SMALL_BUTTON_WIDTH - 10,
                Constants.HEIGHT - Constants.SMALL_BUTTON_HEIGHT - 10
                , new Action() {
            @Override
            public void doAction() {
                //TODO init valuse add to system manager
            }
        }));

        buttons.add(new BackButton(new Action() {
            @Override
            public void doAction() {
                setState(StateMachine.MENU);
            }
        }));

    }

    @Override
    public void update(double delta) {
        for (Button button : buttons) {
            button.update();
        }

    }

    @Override
    public void draw(Graphics2D g2d) {
        int w = highscoreBox.getWidth();
        int h = highscoreBox.getHeight();
        int wPadding = (Constants.WIDTH - w) / 2;
        int hPadding = (Constants.HEIGHT - h) / 2;

        g2d.drawImage(assets.getBackground(("MENU_BACKGROUND")), 0, 0, null);
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.85f));
        g2d.drawImage(highscoreBox, wPadding,
                hPadding, null);
        g2d.setFont(assets.getFont("MAIN").deriveFont(25f));
        g2d.setColor(Color.YELLOW);

        int i = 1;
        int x = 100;
        int initialHorizontalSpacing = wPadding + 40;
        int initialVerticalSpacing = hPadding + 150;
        g2d.drawString("Rank", initialHorizontalSpacing, initialVerticalSpacing);
        g2d.drawString("Name", initialHorizontalSpacing + x, initialVerticalSpacing);
        g2d.drawString("Waves", initialHorizontalSpacing + x * 3, initialVerticalSpacing);
        g2d.drawString("Score", (int) (initialHorizontalSpacing + x * 4.2), initialVerticalSpacing);
        g2d.drawString("Duration", (int) (initialHorizontalSpacing + x * 6), initialVerticalSpacing);
        g2d.setColor(Color.WHITE);
        g2d.setFont(assets.getFont("MAIN").deriveFont(20f));

        for (RankContainer rankContainer : allPlayersGames) {
            if (i > 10) {
                break;
            }

            g2d.drawString(String.valueOf(i), initialHorizontalSpacing, initialVerticalSpacing + i * 60);
            g2d.drawString(rankContainer.getName(), initialHorizontalSpacing + x,
                    initialVerticalSpacing + i * 60);
            g2d.drawString(String.valueOf(rankContainer.getWavesPassed()), initialHorizontalSpacing + x * 3,
                    initialVerticalSpacing + i * 60);
            g2d.drawString(String.valueOf(rankContainer.getScore()), (int) (initialHorizontalSpacing + x * 4.2),
                    initialVerticalSpacing + i * 60);
            String duration = convertToMinutes(rankContainer.getDuration());
            g2d.drawString(duration, (int) (initialHorizontalSpacing + x * 6),
                    initialVerticalSpacing + i * 60);

            i++;

        }

        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
        for (Button button : buttons) {
            button.draw(g2d);
        }

    }

    private String convertToMinutes(long duration) {
        String minutes = (duration/60)+"m";
        String seconds = (duration%60)+"s";

        if(minutes.equals("0m"))return seconds;

        return minutes+" "+seconds;

    }


    @Override
    public void init(Canvas canvas) {
        MouseInput mouseInput = new MouseInput();
        canvas.addMouseMotionListener(mouseInput);
        canvas.addMouseListener(mouseInput);

    }

    private void getPlayers() {
        ArrayList<PlayerProperties> playersProperties = new ArrayList<>();

        PlayersManager playersManager = PlayersManager.getInstance();

        for (String name : playersManager.getAllPlayersNames()) {
            playersProperties.add(playersManager.getProperties(name));

        }


        for (PlayerProperties playerProperties : playersProperties) {
            int gamesPlayer = playerProperties.getScores().size();

            for (int i = 0; i < gamesPlayer; i++) {
                int score = playerProperties.getScores().get(i);
                long duration = playerProperties.getDurations().get(i);
                int waves = playerProperties.getWavesPassed().get(i);

                RankContainer rankContainer = new RankContainer(playerProperties.getName(), score, waves, duration);
                allPlayersGames.add(rankContainer);

            }


        }


    }

    private void sort() {


        allPlayersGames.sort(new WaveSorter()
                .thenComparing(new ScoreSorter())
                .thenComparing(new DurationSorter()));

        Collections.reverse(allPlayersGames);

        int i = 1;
        for (RankContainer rankContainer : allPlayersGames) {
            rankContainer.setRank(i);
            i++;
        }

    }

}


class WaveSorter implements Comparator<RankContainer> {

    @Override
    public int compare(RankContainer o1, RankContainer o2) {
        return o1.getWavesPassed() - o2.getWavesPassed();
    }
}

class DurationSorter implements Comparator<RankContainer> {

    @Override
    public int compare(RankContainer o1, RankContainer o2) {
        return (int) (o2.getDuration() - o1.getDuration());
    }
}

class ScoreSorter implements Comparator<RankContainer> {

    @Override
    public int compare(RankContainer o1, RankContainer o2) {
        return o1.getScore() - o2.getScore();
    }
}
