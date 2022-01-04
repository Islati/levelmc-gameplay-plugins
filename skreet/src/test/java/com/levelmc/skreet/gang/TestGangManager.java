package com.levelmc.skreet.gang;

import com.levelmc.skreet.SkreetTest;
import com.levelmc.skreet.gangs.GangManager;
import com.levelmc.skreet.gangs.GangType;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

public class TestGangManager extends SkreetTest {

    @Test
    public void testGangManager() {
        GangManager gangManager = getPlugin().getGangManager();
        Assertions.assertNotNull(gangManager);

        for (GangType type : GangType.values()) {
            Assertions.assertNotNull(gangManager.getGang(type));
        }
    }

}
