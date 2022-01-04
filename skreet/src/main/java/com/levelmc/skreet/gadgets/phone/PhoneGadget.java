package com.levelmc.skreet.gadgets.phone;

import com.levelmc.core.api.events.gadget.GadgetUseEvent;
import com.levelmc.core.api.gadgets.ItemGadget;
import com.levelmc.core.api.gadgets.interactions.GadgetBreakAction;
import com.levelmc.core.api.item.ItemBuilder;
import com.levelmc.skreet.Skreet;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

/**
 * This mother fucker holds all the modules for our outlaws.
 */
public class PhoneGadget extends ItemGadget {

    private PhoneGadget instance = null;

    public PhoneGadget() {
        super(Skreet.getInstance(),"phone", ItemBuilder.of(Material.AMETHYST_SHARD).name("&bCell Phone").lore("&7..."));
        properties().breakable(false).droppable(false);

    }

    @Override
    public void onBreak(Player p) {
        super.onBreak(p);
    }

    @Override
    public void onInventoryInteraction(InventoryClickEvent e) {
        e.setCancelled(true);
    }



    @Override
    public void onGadgetUse(GadgetUseEvent e) {
        if (e.isRightClick()) {

        }
    }
}
