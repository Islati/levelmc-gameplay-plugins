package com.levelmc.loot.api.repairing;

import com.levelmc.core.api.events.gadget.GadgetUseEvent;
import com.levelmc.core.api.gadgets.ItemGadget;
import com.levelmc.core.api.gadgets.interactions.GadgetInteractionType;
import com.levelmc.core.api.gadgets.interactions.GadgetUseAction;
import com.levelmc.core.api.item.ItemBuilder;
import com.levelmc.core.chat.Chat;
import com.levelmc.loot.Loot;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class ItemRepairScroll extends ItemGadget {
    private static ItemRepairScroll instance = null;

    public static ItemRepairScroll getInstance() {
        if (instance == null) {
            instance = new ItemRepairScroll();
        }

        return instance;
    }

    protected ItemRepairScroll() {
        super(Loot.getInstance(), "item-repair-scroll", ItemBuilder.of(Material.PAPER).name("&6Item Repair Scroll").lore("&bDrop onto an item to repair it."));

        on(GadgetInteractionType.USE, new GadgetUseAction() {
            @Override
            public void onGadgetUse(GadgetUseEvent e) {
                Chat.actionMessage(e.getPlayer(), "&aOpen your inventory and drop this to an item that needs repair.");

            }
        });
    }
}
