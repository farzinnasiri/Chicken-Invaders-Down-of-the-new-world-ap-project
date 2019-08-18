package ir.farzinnasiri.Client.views;

import ir.farzinnasiri.Utils.ObjectPool;

import java.util.List;

public class DrawableObjectPool extends ObjectPool<DrawableObject> {


    public DrawableObjectPool(List<DrawableObject> inUse) {
        super(inUse);
    }

    @Override
    protected DrawableObject create() {
        return new DrawableObject();
    }
}
