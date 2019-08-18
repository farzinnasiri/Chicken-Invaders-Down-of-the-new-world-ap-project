package ir.farzinnasiri.Utils;

import ir.farzinnasiri.Client.systemManager.ConfigsManager;

import java.awt.*;
import java.awt.image.BufferedImage;

import java.util.*;


public class Assets {
    private static Assets assets;

    private ConfigsManager configsManager;

    private HashMap<String, String> basicConfigs;

    private HashMap<String, BufferedImage> weapons;

    private HashMap<String, BufferedImage> boxes;

    private HashMap<String,BufferedImage> buttons;

    private HashMap<String, BufferedImage> bars;

    private HashMap<String, BufferedImage> extras;


    private LinkedHashMap<String, BufferedImage> spaceships;

    private LinkedHashMap<String, BufferedImage[]> exhausts;


    private LinkedHashMap<String, BufferedImage> spaceshipColorIcons;


    private LinkedHashMap<String, BufferedImage> engineColorIcons;


    private HashMap<String, BufferedImage> backgrounds;

    private HashMap<String,BufferedImage> otherImageAssets;


    //enemy
    private LinkedHashMap<String, BufferedImage[]> enemies;
    private HashMap<String, Font> fonts;
    private HashMap<String, BufferedImage[]> effects;


    private Assets() {
        configsManager = new ConfigsManager();
        load();
    }

    public static Assets getInstance() {
        if (assets == null) {
            assets = new Assets();
        }
        return assets;

    }

    public BufferedImage getWeapon(String key) {
        return weapons.get(key);
    }

    public BufferedImage getBox(String key) {
        return boxes.get(key);
    }

    public BufferedImage getSpaceShip(String key){
        return spaceships.get(key);
    }

    public BufferedImage getButton(String key){
        return buttons.get(key);
    }
    public BufferedImage getBar(String key){return bars.get(key);}
    public BufferedImage getBackground(String key) {
        return backgrounds.get(key);
    }
    public BufferedImage getOtherImageAssets(String key){
        return otherImageAssets.get(key);
    }
    public Font getFont(String key){
        return fonts.get(key);
    }
    public LinkedHashMap<String, BufferedImage> getSpaceshipColorIcons() {
        return spaceshipColorIcons; }

    public LinkedHashMap<String, BufferedImage> getEngineColorIcons() {
        return engineColorIcons; }

    public BufferedImage[] getExhausts(String key){
        return exhausts.get(key);
    }
    public BufferedImage[] getEffect(String key){
        return effects.get(key);
    }
    public BufferedImage[] getEnemy(String key){return enemies.get(key);}
    public BufferedImage getExtras(String key){return extras.get(key);}


    private void load() {


        loadBasicConfigs();
        loadWeapons();
        loadBoxes();
        loadSpaceShips();
        loadSpaceshipIcons();
        loadEngineIcons();
        loadButtons();
        loadBars();
        loadBackgrounds();
        loadOtherImageAssets();
        loadFonts();
        loadExhausts();
        loadEffects();
        loadEnemies();
        loadExtras();






    }

    private void loadExtras() {
        HashMap<String,String> namePath = configsManager.getAllProperties("extrasConfigs");

        extras = new HashMap<>();

        for(Map.Entry<String, String> entry:namePath.entrySet()){
            extras.put(entry.getKey(),Loader.imageLoader(entry.getValue()));
        }


    }

    private void loadFonts() {
        HashMap<String,String> namePath = configsManager.getAllProperties("fontConfigs");
        fonts =  new HashMap<>();

        for(Map.Entry<String, String> entry:namePath.entrySet()){
            fonts.put(entry.getKey(),Loader.loadFont(entry.getValue()));
        }
        
    }

    private void loadEnemies(){
        HashMap<String,String> namePath = configsManager.getAllProperties("enemyConfigs");
        enemies = new LinkedHashMap<>();

        for(Map.Entry<String,String> entry:namePath.entrySet()) {

            switch (entry.getKey()){
                case "GREEN_CHICKENS":
                    enemies.put(entry.getKey(),getAllFrames(7,entry.getValue()));
                    break;
                case "BLUE_CHICKENS":
                    enemies.put(entry.getKey(),getAllFrames(7,entry.getValue()));
                    break;
                case "PINK_CHICKENS":
                    enemies.put(entry.getKey(),getAllFrames(7,entry.getValue()));
                    break;
                case "YELLOW_CHICKENS":
                    enemies.put(entry.getKey(),getAllFrames(7,entry.getValue()));
                    break;

                case "BOSS2":
                    enemies.put(entry.getKey(),getAllFrames(360,entry.getValue()));
                    break;
                case "SWIMMER":
                    enemies.put(entry.getKey(),getAllFrames(5,entry.getValue()));
                    break;
                case "DANCER":
                    enemies.put(entry.getKey(),getAllFrames(80,entry.getValue()));
                    break;
            }

        }




    }

    private BufferedImage[] getAllFrames(int numberOfFrames,String path){
        BufferedImage[] frames = new BufferedImage[numberOfFrames];

        for(int i = 0 ; i<numberOfFrames;i++){
            frames[i] = (Loader.imageLoader(String.format(path+"/frame (%d).png",i + 1)));
        }

        return frames;
    }

    private void loadBackgrounds() {
        HashMap<String,String> namePath = configsManager.getAllProperties("backgroundsConfigs");
        backgrounds = new HashMap<>();

        for(Map.Entry<String,String> entry:namePath.entrySet()){
            backgrounds.put(entry.getKey(),Loader.imageLoader(entry.getValue()));

        }

    }

    private void loadButtons() {
        HashMap<String,String> namePath = configsManager.getAllProperties("buttonsConfigs");
        buttons = new HashMap<>();

        for(Map.Entry<String,String> entry:namePath.entrySet()){
            buttons.put(entry.getKey(),Loader.imageLoader(entry.getValue()));
        }



    }
    private void loadBars(){
        HashMap<String,String> namePath = configsManager.getAllProperties("barsConfigs");
        bars = new HashMap<String,BufferedImage>();

        for(Map.Entry<String ,String> entry:namePath.entrySet()){
            bars.put(entry.getKey(),Loader.imageLoader(entry.getValue()));
        }

    }
    private void loadExhausts(){
        HashMap<String,String> namePath = configsManager.getAllProperties("exhaustConfigs");

        exhausts = new LinkedHashMap<>();

        BufferedImage[] fireFrames = new BufferedImage[21];
        for(Map.Entry<String,String> entry:namePath.entrySet()) {
            for (int i = 0; i < 21; i++) {
                fireFrames[i] = Loader.imageLoader(String.format(entry.getValue()+"/(%d).png",i + 1));
            }
            exhausts.put(entry.getKey(),fireFrames);
            fireFrames = new BufferedImage[21];
        }

    }

    private void loadEffects() {
        HashMap<String,String> namePath = configsManager.getAllProperties("effectsConfigs");

        effects = new HashMap<>();

        BufferedImage[] frames = new BufferedImage[74];

        for (Map.Entry<String,String> entry:namePath.entrySet()) {
            for (int i = 0; i < 74; i++) {
                frames[i] = Loader.imageLoader(String.format(entry.getValue()+"/(%d).png", i + 1));
            }
            effects.put(entry.getKey(),frames);
            frames = new BufferedImage[74];
        }
    }

    public void loadBasicConfigs() {
        basicConfigs = configsManager.getAllProperties("basicConfigs");
    }

    public String getBasicConfigProperty(String property) {
        return basicConfigs.get(property);
    }

    private void loadWeapons() {
        HashMap<String, String> namePath = configsManager.getAllProperties("weaponsConfigs");
        weapons = new HashMap<>();

        for (Map.Entry<String, String> entry : namePath.entrySet()) {
            weapons.put(entry.getKey(), Loader.imageLoader(entry.getValue()));

        }


    }


    private void loadBoxes() {
        HashMap<String, String> namePath = configsManager.getAllProperties("gameBoxesConfigs");
        boxes = new HashMap<>();

        for (Map.Entry<String, String> entry : namePath.entrySet()) {
            boxes.put(entry.getKey(), Loader.imageLoader(entry.getValue()));
        }
    }

    private void loadSpaceShips(){
        HashMap<String, String> namePath = configsManager.getAllProperties("spaceShipConfigs");
        spaceships = new LinkedHashMap<>();

        for(Map.Entry<String,String> entry:namePath.entrySet()){
            spaceships.put(entry.getKey(),Loader.imageLoader(entry.getValue()));
        }

    }

    private void loadSpaceshipIcons(){
        loadIcons("spaceshipColorIconsConfigs");


    }
    private void loadEngineIcons(){
        loadIcons("engineColorIconsConfigs");


    }

    private void loadIcons(String kind) {
        HashMap<String, String> namePath = configsManager.getAllProperties(kind);
        LinkedHashMap<String,BufferedImage> linkedHashMap = new LinkedHashMap<>();

        for(Map.Entry<String,String> entry:namePath.entrySet()){
            linkedHashMap.put(entry.getKey(), Loader.imageLoader(entry.getValue()));
        }
        if(kind.equals("spaceshipColorIconsConfigs")){
            spaceshipColorIcons = linkedHashMap;
        }else if(kind.equals("engineColorIconsConfigs")){
             engineColorIcons = linkedHashMap;
        }
    }

    private void loadOtherImageAssets(){
        HashMap<String, String> namePath = configsManager.getAllProperties("otherImageAssetsConfigs");
        otherImageAssets = new HashMap<>();

        for(Map.Entry<String,String> entry:namePath.entrySet()){
            otherImageAssets.put(entry.getKey(),Loader.imageLoader(entry.getValue()));
        }

    }



}
