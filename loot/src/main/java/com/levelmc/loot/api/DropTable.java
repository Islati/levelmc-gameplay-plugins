package com.levelmc.loot.api;

import com.levelmc.core.api.Chanceable;
import com.levelmc.core.api.yml.Path;
import com.levelmc.loot.Loot;

/**
 * Adds a chance and measure to a LootTable.
 * eg. 20 chance, and 100 measure = 20% drop chance
 */
public class DropTable extends LootTable implements Chanceable {

    @Path("chance")
    private int chance;

    @Path("measure")
    private int measure;

    public DropTable(String id, int chance, int measure) {
        super(id);
        this.chance = chance;
        this.measure = measure;
    }

    public DropTable() {

    }

    @Override
    public int getChance() {
        return chance;
    }

    @Override
    public int getMeasure() {
        return measure;
    }

    public void register() {
        Loot.getInstance().getRegistry().addDropTable(this);
    }
}
