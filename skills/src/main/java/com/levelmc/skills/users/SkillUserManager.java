package com.levelmc.skills.users;

import com.levelmc.core.api.players.UserManager;
import com.levelmc.skills.Skills;

public class SkillUserManager extends UserManager<SkillUser> {
    public SkillUserManager() {
        super(Skills.getInstance(), SkillUser.class);
    }
}
