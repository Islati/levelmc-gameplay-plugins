package com.levelmc.core.api.gadgets.interactions;

import com.levelmc.core.api.events.gadget.GadgetUseEvent;

/**
 * When a player uses a gadget with a right or left click.
 */
public interface GadgetUseAction extends GadgetAction {

    /**
     * Defining gadget use logic
     * @param e event which sparks the trigger
     */
    default void onGadgetUse(GadgetUseEvent e) {

    }
}
