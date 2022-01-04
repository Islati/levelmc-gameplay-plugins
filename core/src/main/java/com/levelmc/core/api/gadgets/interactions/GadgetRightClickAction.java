package com.levelmc.core.api.gadgets.interactions;

import com.levelmc.core.api.events.gadget.GadgetUseEvent;
import com.levelmc.core.api.gadgets.Gadget;

/**
 * Action on right click.
 * {@link Gadget}
 */
public interface GadgetRightClickAction extends GadgetUseAction {
    @Override
    default void onGadgetUse(GadgetUseEvent e) {
        if (e.isRightClick()) {
            onRightClick(e);
        }
    }

    /**
     * On right click
     *
     * @param e event triggered
     */
    void onRightClick(GadgetUseEvent e);
}
