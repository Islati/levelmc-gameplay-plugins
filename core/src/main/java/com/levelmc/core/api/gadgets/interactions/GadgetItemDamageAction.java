package com.levelmc.core.api.gadgets.interactions;

import com.levelmc.core.api.gadgets.Gadget;
import org.bukkit.event.player.PlayerItemDamageEvent;

public interface GadgetItemDamageAction extends GadgetAction {
    void onItemDamaged(Gadget gadget, PlayerItemDamageEvent e);
}
