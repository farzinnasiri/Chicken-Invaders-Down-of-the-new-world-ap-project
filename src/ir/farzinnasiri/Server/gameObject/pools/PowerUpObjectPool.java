package ir.farzinnasiri.Server.gameObject.pools;

import ir.farzinnasiri.Server.gameObject.extras.PowerUp;
import ir.farzinnasiri.Utils.ObjectPool;

import java.util.List;

public class PowerUpObjectPool extends ObjectPool<PowerUp> {
    public PowerUpObjectPool(List<PowerUp> inUse) {
        super(inUse);
    }

    @Override
    protected PowerUp create() {
        return new PowerUp();
    }
}
