package ir.farzinnasiri.Client.menus.options;

import ir.farzinnasiri.Client.audio.AudioManager;
import ir.farzinnasiri.Client.buttons.*;
import ir.farzinnasiri.Client.buttons.Button;
import ir.farzinnasiri.Client.input.MouseInput;
import ir.farzinnasiri.Client.stateMachine.StateMachine;
import ir.farzinnasiri.Client.stateMachine.SuperStateMachine;
import ir.farzinnasiri.Client.systemManager.ConfigsManager;
import ir.farzinnasiri.Utils.Assets;
import ir.farzinnasiri.Utils.Constants;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class SoundSettings extends SuperStateMachine {

    private BufferedImage muteBtn;
    private ArrayList<Button> buttons;
    private Assets assets;
    private Integer inGameMusicLevel;
    private Integer mainMenuMusicLevel;
    private Integer creditsMusicLevel;
    private Integer inGameSoundEffectsLevel;
    private Boolean mute;

    private ArrayList<Integer> soundLevels;
    private String[] soundNames;

    private AudioManager audioManager;





    public SoundSettings(StateMachine stateMachine) {
        super(stateMachine);
        assets = Assets.getInstance();


        audioManager = AudioManager.getInstance();

        buttons = new ArrayList<>();
        inGameMusicLevel =  Integer.parseInt(assets.getBasicConfigProperty("inGameMusic"));
        mainMenuMusicLevel = Integer.parseInt(assets.getBasicConfigProperty("mainMenuMusic"));
        creditsMusicLevel = Integer.parseInt(assets.getBasicConfigProperty("creditsMusic"));
        inGameSoundEffectsLevel = Integer.parseInt(assets.getBasicConfigProperty("inGameSoundEffects"));
        mute = Boolean.parseBoolean(assets.getBasicConfigProperty("MUTE"));

        soundLevels = new ArrayList<>();
        soundNames = new String[]{"Game Music","Main Theme","Credits","Sound Effects"};

        loadSoundsLevels();
        setButtons();

    }

    private void loadSoundsLevels(){
        soundLevels.clear();
        soundLevels.add(inGameMusicLevel);
        soundLevels.add(mainMenuMusicLevel);
        soundLevels.add(creditsMusicLevel);
        soundLevels.add(inGameSoundEffectsLevel);

    }

    private void setButtons() {
        buttons.add(new BackButton(new Action() {
            @Override
            public void doAction() {
                setState(StateMachine.OPTIONS);
                saveSettings();
                assets.loadBasicConfigs();
                audioManager.setVolumes();
            }
        }));


            buttons.add(new IncreaseVolumeButton(Constants.HEIGHT / 3 , new Action() {
            @Override
            public void doAction() {
                if(inGameMusicLevel < 10) {
                    inGameMusicLevel++;
                    soundLevels.set(0,inGameMusicLevel);
                }

            }
        }));

        buttons.add(new IncreaseVolumeButton(Constants.HEIGHT / 3 +  70, new Action() {
            @Override
            public void doAction() {
                if(mainMenuMusicLevel < 10) {
                    mainMenuMusicLevel++;
                    soundLevels.set(1,mainMenuMusicLevel);
                    audioManager.setMainThemeVolume(mainMenuMusicLevel);
                }


            }
        }));
        buttons.add(new IncreaseVolumeButton(Constants.HEIGHT / 3 + 140, new Action() {
            @Override
            public void doAction() {
                if(creditsMusicLevel < 10) {
                    creditsMusicLevel++;
                    soundLevels.set(2,creditsMusicLevel);
                }

            }
        }));
        buttons.add(new IncreaseVolumeButton(Constants.HEIGHT / 3 + 210, new Action() {
            @Override
            public void doAction() {
                if(inGameSoundEffectsLevel < 10) {
                    inGameSoundEffectsLevel++;
                    soundLevels.set(3,inGameSoundEffectsLevel);
                    audioManager.setSoundsVolume(inGameSoundEffectsLevel);
                }

            }
        }));
        buttons.add(new DecreaseVolumeButton(Constants.HEIGHT / 3 , new Action() {
            @Override
            public void doAction() {
                if(inGameMusicLevel >0) {
                    inGameMusicLevel--;
                    soundLevels.set(0,inGameMusicLevel);

                }

            }
        }));

        buttons.add(new DecreaseVolumeButton(Constants.HEIGHT / 3 +  70, new Action() {
            @Override
            public void doAction() {
                if(mainMenuMusicLevel >0) {
                    mainMenuMusicLevel--;
                    soundLevels.set(1,mainMenuMusicLevel);
                    audioManager.setMainThemeVolume(mainMenuMusicLevel);


                }

            }
        }));
        buttons.add(new DecreaseVolumeButton(Constants.HEIGHT / 3 + 140, new Action() {
            @Override
            public void doAction() {
                if(creditsMusicLevel >0) {
                    creditsMusicLevel--;
                    soundLevels.set(2,creditsMusicLevel);
                }

            }
        }));
        buttons.add(new DecreaseVolumeButton(Constants.HEIGHT / 3 + 210, new Action() {
            @Override
            public void doAction() {
                if(inGameSoundEffectsLevel > 0) {
                    inGameSoundEffectsLevel--;
                    soundLevels.set(3,inGameSoundEffectsLevel);
                    audioManager.setSoundsVolume(inGameSoundEffectsLevel);



                }

            }
        }));

        buttons.add(new MuteButton(assets.getButton("MUTE_OFF"), assets.getButton("MUTE_ON"),
                100, 100, new Action() {
            @Override
            public void doAction() {
                    if(mute){
                        mute = false;
                    }else{
                        mute = true;
                    }
                    audioManager.setMute(mute);

            }
        }));


    }

    public void saveSettings() {
        ConfigsManager configsManager = new ConfigsManager();
        configsManager.saveProperty("basicConfigs", "inGameMusic",
                Integer.toString(inGameMusicLevel));
        configsManager.saveProperty("basicConfigs", "mainMenuMusic",
                Integer.toString(mainMenuMusicLevel));
        configsManager.saveProperty("basicConfigs", "creditsMusic",
                Integer.toString(creditsMusicLevel));
        configsManager.saveProperty("basicConfigs", "inGameSoundEffects",
                Integer.toString(inGameSoundEffectsLevel));

    }



    @Override
    public void update(double delta) {
        for (Button button : buttons) {
            button.update();

        }



    }

    @Override
    public void draw(Graphics2D g2d) {

        g2d.drawImage(assets.getBackground(("MENU_BACKGROUND")),0,0,null);

        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,0.85f));


        for (Button button : buttons) {
            button.draw(g2d);
        }

        g2d.setColor(Color.WHITE);
        g2d.setFont(assets.getFont("MAIN").deriveFont(30f));
        int j = 0;
        for (Integer level : soundLevels) {
            g2d.drawString(soundNames[j],100, Constants.HEIGHT / 3 +20+ j*70);
            for(int i = 0 ; i < level;i++){
                g2d.fillRect(3*Constants.WIDTH/4 -150+(i+1)*(200/11)+i*10,
                        Constants.HEIGHT / 3 + j*70,10,60);
            }
            j++;

        }


        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,1));
    }

    @Override
    public void init(Canvas canvas) {
        MouseInput mouseInput = new MouseInput();
        canvas.addMouseMotionListener(mouseInput);
        canvas.addMouseListener(mouseInput);

    }
}
