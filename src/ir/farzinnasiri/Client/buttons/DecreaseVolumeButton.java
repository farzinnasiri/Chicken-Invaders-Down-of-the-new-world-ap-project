package ir.farzinnasiri.Client.buttons;


import ir.farzinnasiri.Utils.Assets;
import ir.farzinnasiri.Utils.Constants;

public class DecreaseVolumeButton extends Button {

    public DecreaseVolumeButton(int y, Action action) {
        super(Assets.getInstance().getButton("DECREASE_VOLUME"),
                Assets.getInstance().getButton("DECREASE_VOLUME"),
                3* Constants.WIDTH/4 - 150-Assets.getInstance().getButton("DECREASE_VOLUME").getWidth()
                ,y, action);
    }
}
