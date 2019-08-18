package ir.farzinnasiri.Server.gameObject.pools;

import ir.farzinnasiri.Server.gameObject.enemies.chickens.Chicken;
import ir.farzinnasiri.Utils.ObjectPool;

import java.util.List;

public class ChickenObjectPool extends ObjectPool<Chicken> {

    public ChickenObjectPool(List<Chicken> inUse) {
        super(inUse);
    }

    @Override
    protected Chicken create() {
        return new Chicken();
    }
}
