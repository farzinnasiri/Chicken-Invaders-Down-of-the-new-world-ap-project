package ir.farzinnasiri.Server.gameObject.pools;

import ir.farzinnasiri.Server.gameObject.extras.Egg;
import ir.farzinnasiri.Utils.ObjectPool;

import java.util.List;

public class EggObjectPool extends ObjectPool<Egg> {

    public EggObjectPool(List<Egg> inUse) {
        super(inUse);
    }

    @Override
    protected Egg create() {
        return new Egg();
    }
}
