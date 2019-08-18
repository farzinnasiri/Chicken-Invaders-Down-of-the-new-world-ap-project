package ir.farzinnasiri.Client.buttons;


import ir.farzinnasiri.Utils.Assets;
import ir.farzinnasiri.Utils.Constants;

public class IncreaseVolumeButton extends Button{


    public IncreaseVolumeButton(int y, Action action) {
        super(Assets.getInstance().getButton("INCREASE_VOLUME"),
                Assets.getInstance().getButton("INCREASE_VOLUME")
                , 3* Constants.WIDTH/4 + 150, y, action);
    }
}
