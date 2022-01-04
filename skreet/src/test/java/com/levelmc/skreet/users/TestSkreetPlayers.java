package com.levelmc.skreet.users;

import be.seeseemelk.mockbukkit.entity.PlayerMock;
import com.levelmc.skreet.SkreetTest;
import com.levelmc.skreet.gangs.GangType;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import java.util.UUID;

public class TestSkreetPlayers extends SkreetTest {
    PlayerMock player = null;
    SkreetPlayers userManager = null;

    @Before
    public void setupSkreetPlayersTest() {
        userManager = getPlugin().getUserManager();
        player = getServer().addPlayer("Islati");
    }

    @Test
    public void testSkreetPlayersAdd() {
        Assertions.assertTrue(userManager.hasData(player));
        SkreetPlayer user = userManager.getUser(player);
        Assertions.assertNotNull(user);
        Assertions.assertNull(userManager.getUser(UUID.randomUUID()));
    }

    @Test
    public void testSkreetPlayerGangStatus() {
        SkreetPlayer user = userManager.getUser(player);
        Assertions.assertFalse(user.hasGang());
        user.setGang(GangType.BLOODS);
        Assertions.assertTrue(user.hasGang());
    }

    @Test
    public void testSkreetPlayersRemove() {
        Assertions.assertTrue(userManager.removeUser(player.getUniqueId()));
    }
}
