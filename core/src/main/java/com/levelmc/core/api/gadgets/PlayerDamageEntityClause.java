package com.levelmc.core.api.gadgets;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public interface PlayerDamageEntityClause {

    boolean canDamage(Player player, LivingEntity entity);
}
