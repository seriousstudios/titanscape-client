package com.jagex.actor;

import com.jagex.actor.model.Model;
import com.jagex.cache.def.ItemDefinition;

public final class Item extends Renderable {

    public final Model getRotatedModel() {
        ItemDefinition itemDef = ItemDefinition.lookup(ID);
        return itemDef.getModel(itemCount);
    }

    public int ID;
    public int x;
    public int y;
    public int itemCount;
}
