package ir.farzinnasiri.Server.gameObject.pools;

import ir.farzinnasiri.Server.gameObject.extras.Food;
import ir.farzinnasiri.Utils.ObjectPool;

import java.util.List;

public class FoodObjectPool extends ObjectPool<Food> {
    public FoodObjectPool(List<Food> inUse) {
        super(inUse);
    }

    @Override
    protected Food create() {
        return new Food();
    }
}
