package ir.farzinnasiri.Client.input;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MouseInput extends MouseAdapter{

    private static int X, Y;


    private static boolean MLB;
    private static boolean MRB;

    private static boolean isEntered;


    private static boolean clicked;

    private static boolean listen;

    public static boolean isListen() {
        return listen;
    }

    public static void setListen(boolean listen) {
        MouseInput.listen = listen;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if(e.getButton() == MouseEvent.BUTTON1) {
            MLB = true;
        }if(e.getButton() == MouseEvent.BUTTON2) {
            MRB = true;
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if(e.getButton() == MouseEvent.BUTTON1) {
            MLB = false;
        } if(e.getButton() == MouseEvent.BUTTON2) {
            MRB = false;
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        X = e.getX();
        Y = e.getY();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        X = e.getX();
        Y = e.getY();
    }

    @Override
    public void mouseEntered(MouseEvent e){
        isEntered = true;


    }
    @Override
    public void mouseExited(MouseEvent e) {
        isEntered = false;
    }

    public static int getX() {
        return X;
    }

    public static int getY() {
        return Y;
    }

    public static boolean isMLB() {
        return MLB;
    }

    public static boolean isMRB() {
        return MRB;
    }
    public static boolean isEntered() {
        return isEntered;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        clicked = true;

    }
    public static boolean isClicked() {
        return clicked;
    }

    public static void setClicked(boolean clicked) {
        MouseInput.clicked = clicked;
    }
}
