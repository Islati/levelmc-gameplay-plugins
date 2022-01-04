package com.levelmc.skills.utils;

import com.levelmc.skills.Skills;
import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class DamageIndicatorUtil {

    private static DamageIndicatorUtil instance = null;

    public static DamageIndicatorUtil getInstance() {
        if (instance == null) {
            instance = new DamageIndicatorUtil();
        }

        return instance;
    }

    protected DamageIndicatorUtil() {

    }

    public boolean isUsable() {
        Plugin damageIndicator = Bukkit.getPluginManager().getPlugin("DamageIndicator");
        return damageIndicator != null;
    }

    protected Plugin getDIPlugin() {
        return Bukkit.getPluginManager().getPlugin("DamageIndicator");
    }

    public void spawnDamageIndicator(LivingEntity entity, String format, EntityDamageEvent.DamageCause cause, double damage) {
        if (!isUsable()) {
            return;
        }

//        Reflect.on(getDIPlugin()).field("damageIndicatorListener").call("handleArmorStand",entity, Chat.colorize(format),cause,damage);
    }

    public void spawnDamageIndicatorDelayed(LivingEntity entity, String format, EntityDamageEvent.DamageCause cause, double damage, long delay) {
        new BukkitRunnable(){

            @Override
            public void run() {
                spawnDamageIndicator(entity,format,cause,damage);
            }
        }.runTaskLater(Skills.getInstance(),delay);
    }
}
