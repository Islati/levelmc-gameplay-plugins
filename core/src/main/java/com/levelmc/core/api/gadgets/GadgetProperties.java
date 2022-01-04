package com.levelmc.core.api.gadgets;

import lombok.ToString;
import com.levelmc.core.api.yml.Path;
import com.levelmc.core.api.yml.YamlConfig;

import java.io.File;

@ToString(of = {"isBreakable", "isDroppable"})
/**
 * All the properties specific to Gadgets, accessed through any Gadget instance, it manages properties specific
 * to each gadget instance.
 */
public class GadgetProperties extends YamlConfig {
    /*
    Whether or not the item is breakable; Has no default value.
     */
    @Path("breakable")
    private boolean isBreakable;

    /*

    Whether or not the item can be dropped; by default it's false.
     */
    @Path("droppable")
    private boolean isDroppable = false;

    @Path("offhand-allowed")
    private boolean offHandEquipable = true;

    public GadgetProperties() {

    }

    public GadgetProperties(File file) {
        super(file);
    }

    public GadgetProperties(boolean isBreakable, boolean isDroppable, boolean offHandEquipable) {
        this.isBreakable = isBreakable;
        this.isDroppable = isDroppable;
        this.offHandEquipable = offHandEquipable;
    }

    /**
     * Change whether or not the gadget is able to be broken (follows durability)
     *
     * @param canBreak value to assign
     * @return the gadgetpropeties.
     */
    public GadgetProperties breakable(boolean canBreak) {
        this.isBreakable = canBreak;
        return this;
    }

    /**
     * Change whether or not the gadget is droppable.
     *
     * @param canDrop value to assign.
     * @return the gadgetproperties
     */
    public GadgetProperties droppable(boolean canDrop) {
        this.isDroppable = canDrop;
        return this;
    }

    public GadgetProperties offHandEquippable(boolean equip) {
        this.offHandEquipable = equip;
        return this;
    }

    /**
     * @return whether or not the gadget is breakable.
     */
    public boolean isBreakable() {
        return isBreakable;
    }

    /**
     * @return whether or not the gadget is droppable.
     */
    public boolean isDroppable() {
        return isDroppable;
    }


    /**
     * Check whether or not the gadget can be equipped in the off-hand slot..
     *
     * @return true if the gadget can be equipped in the off-hand slot, false otherwise.
     */
    public boolean isOffhandEquippable() {
        return offHandEquipable;
    }
}
