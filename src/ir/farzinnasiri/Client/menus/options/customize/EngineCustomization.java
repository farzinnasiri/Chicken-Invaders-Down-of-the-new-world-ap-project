package ir.farzinnasiri.Client.menus.options.customize;

import ir.farzinnasiri.Utils.PlayerProperties;
import ir.farzinnasiri.Utils.Assets;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.LinkedHashMap;

public class EngineCustomization  {
    private ColorPicker colorPicker;
    private LinkedHashMap<String, BufferedImage> colorIcons;
    private PlayerProperties playerProperties;

    public EngineCustomization(PlayerProperties playerProperties) {

        this.colorIcons = Assets.getInstance().getEngineColorIcons();
        this.playerProperties = playerProperties;

        colorPicker = new ColorPicker(colorIcons,playerProperties.getEngineColor());
    }


    public void update() {
        colorPicker.update();
        playerProperties.setEngineColor(colorPicker.getColor());
    }

    public void draw(Graphics2D g2d) {
        colorPicker.draw(g2d);

    }


}
