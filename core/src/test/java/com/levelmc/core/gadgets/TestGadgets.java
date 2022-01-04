package com.levelmc.core.gadgets;

import be.seeseemelk.mockbukkit.entity.PlayerMock;
import com.levelmc.core.LevelCoreTest;
import com.levelmc.core.api.events.gadget.GadgetUseEvent;
import com.levelmc.core.api.gadgets.Gadgets;
import com.levelmc.core.api.gadgets.ItemGadget;
import com.levelmc.core.api.gadgets.interactions.GadgetInteractionType;
import com.levelmc.core.api.gadgets.interactions.GadgetUseAction;
import com.levelmc.core.api.item.ItemBuilder;
import com.levelmc.core.chat.Chat;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

public class TestGadgets extends LevelCoreTest {
    private PlayerMock admin;
    private TestGadget gadget;

    class TestGadget extends ItemGadget {

        public TestGadget(Plugin plugin, String id, ItemBuilder builder) {
            super(plugin, id, builder);
        }

        public TestGadget(Plugin plugin, String id, ItemStack item) {
            super(plugin, id, item);
        }
    }

    @Before
    public void setupTests() {
        admin = getServerMock().addPlayer("Islati");
        Gadgets.init(getPlugin());
        gadget = new TestGadget(getPlugin(), "test-gadget", new ItemStack(Material.WOODEN_SWORD));
        Gadgets.registerGadget(getPlugin(), gadget);

        Assertions.assertTrue(Gadgets.isGadget("test-gadget"));
        Assertions.assertTrue(Gadgets.hasBeenRegistered(gadget));
    }

    @Test
    public void testGadgetInteraction() {
        gadget.on(GadgetInteractionType.USE, new GadgetUseAction() {
            @Override
            public void onGadgetUse(GadgetUseEvent e) {
                Chat.message(e.getPlayer(), "Test complete");
            }
        });

        Assertions.assertTrue(gadget.hasAction(GadgetInteractionType.USE));
    }
}
