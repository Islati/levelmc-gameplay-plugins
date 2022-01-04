package com.levelmc.wizards.spells;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

public enum SpellBindSlot {
    RIGHT_CLICK("right_click"),
    LEFT_CLICK("left_click"),
    SHIFT_RIGHT_CLICK("shift_right_click"),
    SHIFT_LEFT_CLICK("shift_left_click");

    private static Map<String, SpellBindSlot> slotNames = new HashMap<>();

    static {
        for (SpellBindSlot slot : SpellBindSlot.values()) {
            slotNames.put(slot.getSlotName(), slot);
        }
    }


    @Getter
    private String slotName = "";

    SpellBindSlot(String slotName) {
        this.slotName = slotName;
    }

    public static SpellBindSlot getSpellBindSlot(String name) {
        return slotNames.get(name);
    }
}
