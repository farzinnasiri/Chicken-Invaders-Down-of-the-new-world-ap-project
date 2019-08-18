package ir.farzinnasiri.Client.audio;

import ir.farzinnasiri.Client.systemManager.ConfigsManager;
import ir.farzinnasiri.Utils.Assets;
import ir.farzinnasiri.Utils.Loader;

import java.util.*;

public class AudioManager {
    private static AudioManager audioManager;
    private ConfigsManager configsManager;
    private Assets assets;
    private HashMap<String, Music> musicList;
    private HashMap<String, Sound> sounds;
    private Random r;

    private Music mainTheme;
    private Music creditsMusic;

    private Boolean mute;
    String randomKey;


    private AudioManager() {

        configsManager = new ConfigsManager();
        assets = Assets.getInstance();
        sounds = new HashMap<>();

        mute = Boolean.parseBoolean(assets.getBasicConfigProperty("MUTE"));

        r = new Random(System.currentTimeMillis());


        loadSounds();
        loadMusics();
        setVolumes();

    }

    public static AudioManager getInstance() {
        if (audioManager == null) {
            audioManager = new AudioManager();
        }
        return audioManager;


    }

    public void setMute(Boolean mute) {
        this.mute = mute;
        configsManager.saveProperty("basicConfigs", "MUTE",
                Boolean.toString(mute));
        assets.loadBasicConfigs();
        setVolumes();


    }

    public Boolean getMute() {
        return mute;
    }


    public void setVolumes() {
        if (!mute) {
            int inGameMusicLevel = Integer.parseInt(assets.getBasicConfigProperty("inGameMusic"));
            int mainMenuMusicLevel = Integer.parseInt(assets.getBasicConfigProperty("mainMenuMusic"));
            int creditsMusicLevel = Integer.parseInt(assets.getBasicConfigProperty("creditsMusic"));
            int inGameSoundEffectsLevel = Integer.parseInt(assets.getBasicConfigProperty("inGameSoundEffects"));

            setSoundsVolume(inGameSoundEffectsLevel);
            setMusicsVolume(inGameMusicLevel);
            mainTheme.setVolume(mainMenuMusicLevel);
            creditsMusic.setVolume(creditsMusicLevel);
        } else {
            setSoundsVolume(0);
            setMusicsVolume(0);
            mainTheme.setVolume(0);
            creditsMusic.setVolume(0);
        }


    }


    private void loadSounds() {
        HashMap<String, String> namePath = configsManager.getAllProperties("soundConfigs");

        for (Map.Entry<String, String> entry : namePath.entrySet()) {
            Sound sound = new Sound(Loader.loadSound(entry.getValue()));
            sounds.put(entry.getKey(), sound);
        }


    }

    private void loadMusics() {
        musicList = new HashMap<>();
        HashMap<String, String> namePath = configsManager.getAllProperties("musicConfigs");

        mainTheme = new Music(Loader.loadSound(namePath.get("MAIN_THEME")));
        mainTheme.setLoop(true);

        creditsMusic = new Music(Loader.loadSound(namePath.get("CREDITS")));

        for (Map.Entry<String, String> entry : namePath.entrySet()) {
            if (!(entry.getKey().equals("MAIN_THEME") ||
                    entry.getKey().equals("CREDITS"))) {
                Music music = new Music(Loader.loadSound(entry.getValue()));
                musicList.put(entry.getKey(), music);
            }

        }


    }

    public void playGameMusics() {
        stopMainTheme();
        if (!mute) {
            List<String> keysAsArray = new ArrayList<>(musicList.keySet());

           randomKey = keysAsArray.get(r.nextInt(keysAsArray.size()));
            if (!randomKey.equals("DANCE")) {
                Music music = musicList.get(randomKey);
                music.play();
                music.setOnEndOfMedia(new Runnable() {
                    @Override
                    public void run() {
                        playGameMusics();
                    }
                });

            }else {
                playGameMusics();
            }


        }


    }

    public void playGameMusic(String musicName){
        Music music = musicList.get(musicName);
        music.play();
        music.setOnEndOfMedia(new Runnable() {
            @Override
            public void run() {
                playGameMusics();
            }
        });

    }

    public void stopGameMusics() {
        for (Music music : musicList.values()) {
            music.stop();

        }

    }

    public void pauseGameMusics() {
        if(musicList.get(randomKey) != null) {
            musicList.get(randomKey).pause();
        }


    }

    public void playSound(String key, boolean reset) {
        if (!mute) {
            if (reset) {
                resetSound(key);
            }
            sounds.get(key).play();
        }


    }

    public void stopSound(String key) {
        //TODO if condition for mute
        if (!sounds.get(key).isPlaying()) {
            sounds.get(key).stop();
        }

    }

    public void pauseSound(String key) {
        //TODO if condition for mute
        sounds.get(key).pause();

    }

    public void setSoundsVolume(int volume) {
        sounds.values().forEach(s -> s.setVolume(volume));

    }

    private void setMusicsVolume(int volume) {
        for (Music music : musicList.values()) {
            music.setVolume(volume);
        }

    }

    public void setMainThemeVolume(int volume) {
        mainTheme.setVolume(volume);
    }


    private void resetSound(String key) {
        sounds.get(key).reset();
    }

    public void playMainTheme() {
        if (!mute) {
            mainTheme.reset();
            mainTheme.play();
        }


    }

    public void stopMainTheme() {
        mainTheme.stop();

    }

    public void playCreditsMusic() {
        if (!mute) {
            creditsMusic.reset();
            creditsMusic.play();
        }

    }

    public void stopCreditsMusic() {
        creditsMusic.stop();

    }


}
