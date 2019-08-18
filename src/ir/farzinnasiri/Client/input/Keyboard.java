package ir.farzinnasiri.Client.input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Keyboard implements KeyListener {
    private int pressed = -1;

    public int pressed() {
        return pressed;
    }
    public void resetKeyboard(){
        pressed = -1;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        pressed = key;

    }

    @Override
    public void keyReleased(KeyEvent e) {


    }
}