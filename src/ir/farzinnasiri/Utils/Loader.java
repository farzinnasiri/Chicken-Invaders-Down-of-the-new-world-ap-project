package ir.farzinnasiri.Utils;


import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javafx.scene.media.Media;

public class Loader {
    public static BufferedImage imageLoader(String path)
    {
        try {
            return ImageIO.read(Loader.class.getResource(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Font loadFont(String path) {
        try {
            return Font.createFont(Font.TRUETYPE_FONT, Loader.class.getResourceAsStream(path));
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Media loadSound(String path) {
        try {

              return new Media(Loader.class.getResource(path).toExternalForm());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
