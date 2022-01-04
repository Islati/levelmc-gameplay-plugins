package com.levelmc.loot.api;

import com.levelmc.core.api.Chanceable;
import com.levelmc.loot.Loot;
import com.levelmc.loot.api.repairing.ItemRepairScroll;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

public class MobDropListener implements Listener {

    private Loot parent;

    public MobDropListener(Loot parent) {
        this.parent = parent;
    }

    @EventHandler
    public void onEntityDeathEvent(EntityDeathEvent e) {
        MobDropData drops = parent.getRegistry().getDropData(e.getEntityType());

        if (drops == null) {
            return;
        }

        DropTable table = drops.getRandomTable();

        if (!table.chanceCheck()) {
            if (Chanceable.check(5, 100)) {
                e.getDrops().add(ItemRepairScroll.getInstance().getItem());
            }
            return;
        }

        e.getDrops().add(table.createItem());
        //todo implement loot indicators.
    }
}
