package com.levelmc.skills;

import com.levelmc.skills.perks.BasePerk;
import com.levelmc.skills.perks.SkillPerk;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import java.util.List;

public class TestSkillsPlugin extends SkillsTest {

    @Test
    public void testSkillsComponentPerkRegistration() {
        Skills.getInstance().registerPerk(
                new BasePerk("test-punch", "Big Punch", SkillType.UNARMED, 5, 5)
        );
        Skills skills = Skills.getInstance();
        Assertions.assertTrue(skills.hasPerks(SkillType.UNARMED));
        Assertions.assertNotNull(skills.getPerk("test-punch"));
        List<SkillPerk> unarmedPerks = skills.getPerks(SkillType.UNARMED);
        Assertions.assertEquals(1, unarmedPerks.size());
        Assertions.assertTrue(skills.hasPerks(SkillType.UNARMED));
    }
}
