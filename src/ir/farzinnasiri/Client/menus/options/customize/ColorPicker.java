package ir.farzinnasiri.Client.menus.options.customize;



import ir.farzinnasiri.Client.buttons.Action;
import ir.farzinnasiri.Client.buttons.Button;
import ir.farzinnasiri.Utils.Constants;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class ColorPicker {
    private LinkedHashMap<String, BufferedImage> colorIcons;
    private ArrayList<Button> icons;

    private String color;

    public ColorPicker(LinkedHashMap<String, BufferedImage> colorIcons,String color) {
        this.colorIcons = colorIcons;
        icons = new ArrayList<>();

        this.color = color;

        setButtons();
    }

    private void setButtons() {
        int row = 0;
        int column = 0;
        BufferedImage icon;
        for (Map.Entry e : colorIcons.entrySet()) {
            if (column == 3) {
                column = 0;
                row++;
            }
            icon = (BufferedImage) e.getValue();
            icons.add(new Button(icon, icon,
                    3 * Constants.WIDTH / 4 + icon.getWidth() * column,
                    Constants.HEIGHT / 3 + icon.getHeight() * row,
                    new Action() {
                        @Override
                        public void doAction() {

                            setColor(e.getKey().toString());

                        }
                    }));
            column++;

        }


    }


    public void update() {
        for (Button icon : icons) {
            icon.update();
        }

    }

    public void draw(Graphics2D g2d) {
        for (Button icon : icons) {
            icon.draw(g2d);
        }

    }

    public String getColor() {
        return color;

    }

    public void setColor(String color) {
        this.color = color;
    }
}
