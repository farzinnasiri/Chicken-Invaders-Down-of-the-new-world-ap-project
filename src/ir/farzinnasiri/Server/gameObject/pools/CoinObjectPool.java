package ir.farzinnasiri.Server.gameObject.pools;

import ir.farzinnasiri.Server.gameObject.extras.Coin;
import ir.farzinnasiri.Utils.ObjectPool;

import java.util.List;

public class CoinObjectPool extends ObjectPool<Coin> {
    public CoinObjectPool(List<Coin> inUse) {
        super(inUse);
    }

    @Override
    protected Coin create() {
        return new Coin();
    }
}
