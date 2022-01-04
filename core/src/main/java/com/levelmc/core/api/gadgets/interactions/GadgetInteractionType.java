package com.levelmc.core.api.gadgets.interactions;

public enum GadgetInteractionType {
    BREAK(GadgetBreakAction.class),
    DROP(GadgetDropAction.class),
    INVENTORY_INTERACTION(GadgetInventoryInteraction.class),
    ITEM_DAMAGE(GadgetItemDamageAction.class),
    LEFT_CLICK_IN_HAND(GadgetLeftClickEvent.class),
    RIGHT_CLICK_IN_HAND(GadgetRightClickAction.class),
    SWITCH_HAND(GadgetSwitchHandAction.class),
    USE(GadgetUseAction.class);

    private Class<? extends GadgetAction> clazz;

    GadgetInteractionType(Class<? extends GadgetAction> interactionClass) {
        this.clazz = interactionClass;
    }

    public Class<? extends GadgetAction> getGadgetActionClass() {
        return this.clazz;
    }
}
