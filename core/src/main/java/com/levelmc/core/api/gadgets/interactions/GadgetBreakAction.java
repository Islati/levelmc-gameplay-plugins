package com.levelmc.core.api.gadgets.interactions;

import com.levelmc.core.api.gadgets.Gadget;
import org.bukkit.entity.Player;

public interface GadgetBreakAction extends GadgetAction {
    void onBreak(Gadget gadget, Player p);
}
