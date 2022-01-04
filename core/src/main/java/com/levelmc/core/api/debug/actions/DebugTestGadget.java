package com.levelmc.core.api.debug.actions;

import com.levelmc.core.LevelCore;
import com.levelmc.core.api.debug.DebugAction;
import com.levelmc.core.api.events.gadget.GadgetUseEvent;
import com.levelmc.core.api.gadgets.Gadgets;
import com.levelmc.core.api.gadgets.ItemGadget;
import com.levelmc.core.api.gadgets.interactions.GadgetInteractionType;
import com.levelmc.core.api.gadgets.interactions.GadgetUseAction;
import com.levelmc.core.api.players.PlayerUtils;
import com.levelmc.core.api.item.ItemBuilder;
import com.levelmc.core.chat.Chat;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class DebugTestGadget extends DebugAction {

    public static class TestGadget extends ItemGadget {
        public TestGadget() {
            super(LevelCore.getInstance(),"tcTestGadget", ItemBuilder.of(Material.STICK).name("&aStick of Debugging").lore("&bRumor has it the admins forgot this when they launched the server"));
            on(GadgetInteractionType.USE, new GadgetUseAction() {
                @Override
                public void onGadgetUse(GadgetUseEvent e) {
                    Chat.actionMessage(e.getPlayer(),"&cYou feel flow through you.");
                }
            });
        }
    }

    private static DebugTestGadget instance = null;

    public static DebugTestGadget getInstance() {
        if (instance == null) {
            instance = new DebugTestGadget();
        }

        return instance;
    }

    private TestGadget gadget = null;
    protected DebugTestGadget() {
        super("tc-debug-gadget");

        Gadgets.registerGadget(LevelCore.getInstance(),gadget = new TestGadget());
    }

    @Override
    public void onDebug(Player player, String[] args) {
        PlayerUtils.giveItem(player,gadget.getItem());
        Chat.msg(player,"&7You have acquired a new tool.");
    }
}
