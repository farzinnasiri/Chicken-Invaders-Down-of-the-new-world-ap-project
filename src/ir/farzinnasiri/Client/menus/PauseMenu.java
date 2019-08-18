package ir.farzinnasiri.Client.menus;

import ir.farzinnasiri.Client.ClientConnectionHandler;
import ir.farzinnasiri.Client.audio.AudioManager;
import ir.farzinnasiri.Client.buttons.Action;
import ir.farzinnasiri.Client.buttons.MuteButton;
import ir.farzinnasiri.Client.input.MouseInput;
import ir.farzinnasiri.Client.buttons.Button;
import ir.farzinnasiri.Client.stateMachine.StateMachine;
import ir.farzinnasiri.Client.stateMachine.SuperStateMachine;
import ir.farzinnasiri.Client.views.GameObjects;
import ir.farzinnasiri.Server.GameState;
import ir.farzinnasiri.Utils.Assets;
import ir.farzinnasiri.Utils.Constants;
import ir.farzinnasiri.Utils.GroupLoader;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class PauseMenu extends SuperStateMachine  {
    private Assets assets;
    private final Cursor knifeCursor;
    private ArrayList<Button> buttons;
    private BufferedImage pauseMenu;
    private int menuUpperMargin;
    private int menuSideMargin;
    private MouseInput mouseInput;
    private AudioManager audioManager;
    private ClientConnectionHandler clientConnectionHandler;


    public PauseMenu(StateMachine stateMachine,ClientConnectionHandler clientConnectionHandler) {
        super(stateMachine);

        assets =Assets.getInstance();

        this.clientConnectionHandler = clientConnectionHandler;
        pauseMenu =assets.getBox("PAUSE");

        buttons = new ArrayList<>();

        audioManager = AudioManager.getInstance();

        audioManager.pauseGameMusics();

        FC fc = new FC();



        knifeCursor = Toolkit.getDefaultToolkit().createCustomCursor(
                assets.getOtherImageAssets("KNIFE"), new Point(0, 0), "knife Cursor");

        menuUpperMargin = (Constants.HEIGHT- pauseMenu.getHeight())/2;
        menuSideMargin = (Constants.WIDTH- pauseMenu.getWidth())/2;
        int buttonSideMargin = (pauseMenu.getWidth()- Constants.BIG_BUTTON_WIDTH)/2;

        mouseInput = new MouseInput();

        //resume button
        buttons.add(new Button(assets.getButton("RESUME_OUT"),assets.getButton("RESUME_IN"),
                menuSideMargin +buttonSideMargin, menuUpperMargin +50,
                new Action() {
                    @Override
                    public void doAction()  {
                        synchronized (clientConnectionHandler){
                            try {
                                clientConnectionHandler.sendCommend("ESCAPE_PRESSED");

                                clientConnectionHandler.wait();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }));

        //quit button
        buttons.add(new Button(assets.getButton("QUIT_GAME_OUT"),assets.getButton("QUIT_GAME_IN"),
                menuSideMargin +buttonSideMargin, menuUpperMargin +150,
                new Action() {
                    @Override
                    public void doAction() {
                        synchronized (clientConnectionHandler){
                            clientConnectionHandler.sendCommend("QUIT");
                            try {
                                clientConnectionHandler.wait();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                            GameObjects.getInstance().init();
                        }
                        setState(StateMachine.GAME_SELECTION);
                        GameObjects.getInstance().init();
                        audioManager.stopGameMusics();

                    }
                }));
        //mute button
        buttons.add(new MuteButton(assets.getButton("MUTECEHCKBOX_UNCHECKED"),
                assets.getButton("MUTECHECKBOX_CHECKED"),
                menuSideMargin +(pauseMenu.getWidth()-assets.getButton("MUTECHECKBOX_CHECKED").getWidth())/2-100,
                menuUpperMargin +250,
                new Action() {
                    @Override
                    public void doAction() {
                    }
                }));

        buttons.add(new Button(assets.getButton("NEW_FEATURE_OUT"), assets.getButton("NEW_FEATURE_IN"),
                menuSideMargin + (pauseMenu.getWidth() - assets.getButton("MUTECHECKBOX_CHECKED").getWidth()) / 2 + 180,
                menuUpperMargin + 250, new Action() {
            @Override
            public void doAction() {
                try {
                    fc.showfileChooser();
                }catch (Exception e){
                    e.printStackTrace();
                }


            }
        }));
    }



    @Override
    public void update(double delta) {

        for(Button button:buttons){
            button.update();
        }


    }

    @Override
    public void draw(Graphics2D g2d) {
        g2d.drawImage(assets.getBackground(("MENU_BACKGROUND")),0,0,null);
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,0.80f));
        g2d.drawImage(pauseMenu, menuSideMargin, menuUpperMargin,null);
        for(Button button:buttons){
            button.draw(g2d);
        }
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,1f));
    }

    @Override
    public void init(Canvas canvas) {
        canvas.setCursor(knifeCursor);
        canvas.addMouseListener(mouseInput);
        canvas.addMouseMotionListener(mouseInput);


    }

    class FC extends JPanel{
        JFileChooser fileChooser;

        public FC(){
             fileChooser= new JFileChooser();



        }

        public void showfileChooser(){
            int result = fileChooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                System.out.println("Selected file: " + selectedFile.getAbsolutePath());
                URL url = null;
                try {

                    url = selectedFile.toURI().toURL();
                } catch (MalformedURLException e) {
                }

                String fullName = "ir.farzinnasiri.Server.gameObject.enemies.groups."+"TurningGroup";
                System.out.println(fullName);
                try {
                    Class c = new GroupLoader().loadClassWithUrl(url, fullName);

                    GameState.getInstance().addNewGroup(c);
                }catch (Exception e){
                    e.printStackTrace();
                }


            }


        }

    }

}