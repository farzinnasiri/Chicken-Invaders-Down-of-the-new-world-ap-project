package ir.farzinnasiri.Server.gameObject.pools;


import ir.farzinnasiri.Server.gameObject.weapons.Laser;
import ir.farzinnasiri.Utils.ObjectPool;
import ir.farzinnasiri.Utils.UniqueId;

import java.util.List;

public class LaserObjectPool extends ObjectPool<Laser> {

    public LaserObjectPool(List<Laser> inUse) {
        super(inUse);
    }

    @Override
    protected Laser create() {
        return new Laser(UniqueId.getIdentifier());
    }
}
