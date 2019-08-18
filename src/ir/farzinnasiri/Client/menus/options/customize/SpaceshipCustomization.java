package ir.farzinnasiri.Client.menus.options.customize;


import ir.farzinnasiri.Utils.PlayerProperties;
import ir.farzinnasiri.Utils.Assets;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.LinkedHashMap;

public class SpaceshipCustomization  {
    private Assets assets;
    private ColorPicker colorPicker;
    private LinkedHashMap<String,BufferedImage> colorIcons;
    private PlayerProperties playerProperties;


    public SpaceshipCustomization(PlayerProperties playerProperties) {

        assets = Assets.getInstance();

        colorIcons = assets.getSpaceshipColorIcons();

        this.playerProperties = playerProperties;


        colorPicker = new ColorPicker(colorIcons,playerProperties.getSpaceshipColor());

    }

    public void update() {
        colorPicker.update();
        playerProperties.setSpaceshipColor(colorPicker.getColor());


    }

    public void draw(Graphics2D g2d) {
        colorPicker.draw(g2d);

    }
}
