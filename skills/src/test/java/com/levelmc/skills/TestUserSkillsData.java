package com.levelmc.skills;

import be.seeseemelk.mockbukkit.entity.PlayerMock;
import com.levelmc.core.api.utils.LevelExpUtil;
import com.levelmc.skills.perks.BasePerk;
import com.levelmc.skills.perks.SkillPerk;
import com.levelmc.skills.users.SkillUser;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

public class TestUserSkillsData extends SkillsTest {

    private PlayerMock mockPlayer = null;
    private SkillUser user = null;

    @Before
    public void setupTest() {
        mockPlayer = getServer().addPlayer("Islati");
        user = getPlugin().getUserManager().getUser(mockPlayer);
    }

    @Test
    public void testUserSkillPoints() {
        for (SkillType skill : SkillType.values()) {
            user.addSkillPoint(skill, 1);
            Assertions.assertEquals(1, user.getSkillPointsAvailable(skill));
        }
    }

    @Test
    public void testUserMiningMasteryPerk() {
        for (SkillType skill : SkillType.values()) {
            Assertions.assertEquals(user.getExperience(skill), LevelExpUtil.getExperienceAtLevel(user.getLevel(skill)));
        }
    }

    @Test
    public void testSubtractSkillPoint() {
        for (SkillType skill : SkillType.values()) {
            user.addSkillPoint(skill, 2);
            Assertions.assertEquals(2,user.getSkillPointsAvailable(skill));
            user.subtractSkillPoint(skill, 1);
            Assertions.assertEquals(1, user.getSkillPointsAvailable(skill));
        }
    }

    @Test
    public void testUserHasPerk() {
        for (SkillType type : SkillType.values()) {
            Skills.getInstance().registerPerk(
                    new BasePerk(type.getSkillName() + "-1", type.getSkillName() + "-1", type, 1, 10)
            );
        }

        for (SkillType type : SkillType.values()) {
            for (SkillPerk perk : Skills.getInstance().getPerks(type)) {
                user.addPerkLevel(perk.getPerkId(), 1);
                Assertions.assertEquals(1,user.getPerkLevel(perk.getPerkId()));
            }
        }
    }

    @Test
    public void testUserPerkLevels() {

        for (SkillType type : SkillType.values()) {
            Skills.getInstance().registerPerk(
                    new BasePerk(type.getSkillName() + "-1", type.getSkillName() + "-1", type, 1, 10)
            );
            user.setPerkLevel(type.getSkillName() + "-1", 2);
            Assertions.assertEquals(2,user.getPerkLevel(type.getSkillName() + "-1"));
            user.addPerkLevel(type.getSkillName() + "-1", 1);
            Assertions.assertEquals(3, user.getPerkLevel(type.getSkillName() + "-1"));
        }


    }
}
