package ir.farzinnasiri.Client.buttons;


import ir.farzinnasiri.Utils.Assets;
import ir.farzinnasiri.Utils.Constants;

public class BackButton extends Button {


    public BackButton(Action action) {
        super(Assets.getInstance().getButton("BACK_OUT"),Assets.getInstance().getButton("BACK_IN")
                , 10, Constants.HEIGHT-Assets.getInstance().getButton("BACK_OUT").getHeight()
                        -10, action);
    }
}
