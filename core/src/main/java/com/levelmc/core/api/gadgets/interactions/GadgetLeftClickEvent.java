package com.levelmc.core.api.gadgets.interactions;

import com.levelmc.core.api.events.gadget.GadgetUseEvent;
import com.levelmc.core.api.gadgets.Gadget;

/**
 * Actions on left click
 * {@link Gadget}
 */
public interface GadgetLeftClickEvent extends GadgetUseAction {

    @Override
    default void onGadgetUse(GadgetUseEvent e) {
        if (e.isLeftClick()) {
            onLeftClick(e);
        }
    }

    /**
     * On left click
     *
     * @param e event trigger
     */
    void onLeftClick(GadgetUseEvent e);

}
