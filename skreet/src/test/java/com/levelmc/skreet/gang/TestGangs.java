package com.levelmc.skreet.gang;

import be.seeseemelk.mockbukkit.entity.PlayerMock;
import com.levelmc.skreet.Skreet;
import com.levelmc.skreet.SkreetTest;
import com.levelmc.skreet.gangs.Gang;
import com.levelmc.skreet.gangs.GangManager;
import com.levelmc.skreet.gangs.GangType;
import com.levelmc.skreet.users.SkreetPlayer;
import com.levelmc.skreet.users.SkreetPlayers;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

public class TestGangs extends SkreetTest {
    private PlayerMock playerMock;
    private SkreetPlayers userManager = null;
    private GangManager gangManager = null;

    @Before
    public void setupTest() {
        userManager = getPlugin().getUserManager();
        gangManager = getPlugin().getGangManager();
        playerMock = getServer().addPlayer("Islati");
        userManager.addUser(new SkreetPlayer(playerMock));
        Assertions.assertNotNull(userManager.getUser(playerMock));
    }

    @Test
    public void testGangAssociations() {
        Gang crip = gangManager.getGang(GangType.CRIPS);
        Assertions.assertFalse(crip.isMember(playerMock));

        SkreetPlayer player = userManager.getUser(playerMock);
        crip.addMember(player);
        Assertions.assertTrue(crip.isMember(player));

        Gang blood = gangManager.getGang(GangType.BLOODS);
        Assertions.assertFalse(blood.isMember(player));
        Assertions.assertFalse(blood.addMember(player));
    }

    @Test
    public void testGangKill() {
        PlayerMock killed = getServer().addPlayer("Dead");
        SkreetPlayer skKilled = userManager.getUser(killed.getUniqueId());
        skKilled.setGang(GangType.BLOODS);
        SkreetPlayer skKiller = userManager.getUser(killed.getUniqueId());
        skKiller.setGang(GangType.CRIPS);
        Gang crip = gangManager.getGang(GangType.CRIPS);
        Gang blood = gangManager.getGang(GangType.BLOODS);
        crip.addKill(skKiller, skKilled, "Test Kill");

        Assertions.assertTrue(crip.getGangKills().size() > 0);
        Assertions.assertEquals(crip.getGangKills().get(0).getKillerId(), skKiller.getId());
        Assertions.assertFalse(blood.getGangKills().size() > 0);
    }
}
