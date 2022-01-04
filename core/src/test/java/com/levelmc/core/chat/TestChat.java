package com.levelmc.core.chat;

import be.seeseemelk.mockbukkit.entity.PlayerMock;
import com.levelmc.core.LevelCoreTest;
import org.junit.Before;
import org.junit.Test;

public class TestChat extends LevelCoreTest {
    private PlayerMock player;

    @Before
    public void setup() {
        player = getServerMock().addPlayer("FreshDaddyB");
    }

    @Test
    public void testChatMsg() {
        Chat.msg(player,"Hello world!");
        player.assertSaid("Hello world!");

        Chat.msg(player,"&7Grey Skies");
        player.assertSaid(Chat.colorize("&7Grey Skies"));

        player.assertNoMoreSaid();
    }

}
