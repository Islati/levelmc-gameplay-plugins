package com.levelmc.wizards.listeners;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.levelmc.core.api.gui.Menu;
import com.levelmc.core.api.utils.ProgressBarUtil;
import com.levelmc.core.chat.Chat;
import com.levelmc.core.api.inventory.HandSlot;
import com.levelmc.core.api.particle.projectiles.ParticleProjectile;
import com.levelmc.core.api.events.particleprojectiles.ParticleProjectileHitBlockEvent;
import com.levelmc.core.api.events.particleprojectiles.ParticleProjectileHitEntityEvent;
import com.levelmc.core.api.players.PlayerUtils;
import com.levelmc.core.api.utils.NumberUtil;
import com.levelmc.core.api.utils.PluginUtils;
import com.levelmc.loot.api.utils.DamageIndicatorUtil;
import com.levelmc.wizards.Wizards;
import com.levelmc.wizards.events.*;
import com.levelmc.wizards.gui.SpellBindsGUI;
import com.levelmc.wizards.spells.Spell;
import com.levelmc.wizards.spells.SpellBindSlot;
import com.levelmc.wizards.users.Wizard;
import com.levelmc.wizards.users.WizardsUserManager;
import com.levelmc.wizards.utils.WizardItemUtils;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class WizardingListener implements Listener {

    private Cache<UUID, Long> userDropCache = CacheBuilder.newBuilder().expireAfterWrite(1, TimeUnit.MILLISECONDS).build();

    private Wizards core;
    private WizardsUserManager users;

    public WizardingListener(Wizards core, WizardsUserManager userManager) {
        this.core = core;
        this.users = userManager;
    }

    @EventHandler
    public void onMagicLevelUpEvent(MagicLevelEvent e) {
        Player p = e.getPlayer();
        int nextLevel = e.getCurrentLevel();

        Chat.sendTitle(p, "&6Level Up!", String.format("&dMagic &7► &dLvl &b%s", nextLevel), 10, 20, 10);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onSpellHitEntityEvent(SpellHitEntityEvent e) {
        //give exp reward.
        Player player = e.getPlayer();
        Wizard user = e.getUser();
        Spell spell = e.getSpell();
        int playerSpellLevel = spell.getPlayerSpellLevel(player);

        MagicExpGainEvent expGainEvent = new MagicExpGainEvent(player, e.getSpell().getExpAward(player, e.getSpell().getPlayerSpellLevel(player)));
        PluginUtils.callEvent(expGainEvent);

        if (expGainEvent.isCancelled()) {
            return;
        }

        int previousLevel = user.getWizardingLevel();
        user.addExperience(expGainEvent.getAmount());
        DamageIndicatorUtil.getInstance().spawnDamageIndicatorDelayed(e.getPlayer(), String.format("&a+ &b%s &aMagic EXP", expGainEvent.getAmount()), EntityDamageEvent.DamageCause.CUSTOM, expGainEvent.getAmount(), 5L);
        int currentLevel = user.getWizardingLevel();

        if (currentLevel > previousLevel) {
            //todo LEVEL UP
            MagicLevelEvent levelEvent = new MagicLevelEvent(player, previousLevel, currentLevel);
            PluginUtils.callEvent(levelEvent);
        }

    }

    @EventHandler
    public void onItemDrop(PlayerDropItemEvent e) {
        Player player = e.getPlayer();
        Item drop = e.getItemDrop();

        ItemStack dropItem = drop.getItemStack();

        if (!core.getWizardItemUtils().isWand(dropItem)) {
            return;
        }

        e.setCancelled(true);
        new SpellBindsGUI(core.getUserManager().getUser(player)).openMenu(player);
        return;
    }

    @EventHandler
    public void onSpellPreCastEvent(SpellPreCastEvent event) {
        Player player = event.getPlayer();
        Spell spell = event.getSpell();
        Wizard user = event.getUser();

        int spellManaCost = spell.getManaCost(player, spell.getPlayerSpellLevel(player));

        if (user.getMana() < spellManaCost) {
            Chat.actionMessage(player, "&c✗ &bNot enough mana&c ✗");
            event.setCancelled(true);
            return;
        }

        user.setMana(user.getMana() - spellManaCost);
        showManaInActionBar(player);
    }

    protected void showManaInActionBar(Player player) {
        Wizard user = Wizards.getInstance().getUserManager().getUser(player);

        double manaPercentLeft = ((double) user.getMana() / (double) user.getMaxMana(player)) * 100;
        int numericPercentLeft = (int) NumberUtil.round(manaPercentLeft, 1);
        Chat.actionMessage(player, ProgressBarUtil.renderProgressBar("&b", "&c", ProgressBarUtil.DEFAULT_BAR, numericPercentLeft));
    }

    @EventHandler
    public void onManaRegentEvent(ManaRegenEvent event) {
        Wizard user = event.getUser();
        Player player = event.getPlayer();

        int maxMana = user.getMaxMana(player);

        if (user.getMana() >= maxMana) {
            return;
        }

        if (user.getMana() + event.getAmount() >= maxMana) {
            user.setMana(maxMana);
        } else {
            user.setMana(user.getMana() + event.getAmount());
        }

        showManaInActionBar(event.getPlayer());
        DamageIndicatorUtil.getInstance().spawnDamageIndicatorDelayed(player, String.format(Chat.colorize("&a✧ + %s Mana ✧"), event.getAmount()), EntityDamageEvent.DamageCause.CUSTOM, event.getAmount(), 5L);
    }

    /**
     * Get the {@link SpellBindSlot} being activated in this event.
     *
     * @param e interaction event being checked
     * @return slot of the spell the player is casting, otherwise null.
     */
    protected SpellBindSlot getChosenActivation(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        Action action = e.getAction();

        if (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) {
            if (p.isSneaking()) {
                return SpellBindSlot.SHIFT_RIGHT_CLICK;
            }
            return SpellBindSlot.RIGHT_CLICK;
        } else if (action == Action.LEFT_CLICK_AIR || action == Action.LEFT_CLICK_BLOCK) {
            if (p.isSneaking()) {
                return SpellBindSlot.SHIFT_LEFT_CLICK;
            }

            return SpellBindSlot.LEFT_CLICK;
        }

        return null;
    }

    @EventHandler
    public void onPlayerInteractHandleWizardItems(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        Action action = e.getAction();

        if (PlayerUtils.handIsEmpty(player, HandSlot.MAIN_HAND)) {
            return;
        }

        /* Fix for spells being cast when menus open */
        if (player.getOpenInventory() instanceof Menu) {
            return;
        }

        ItemStack mainhand = PlayerUtils.getItemInHand(player, HandSlot.MAIN_HAND);

        WizardItemUtils utils = core.getWizardItemUtils();

        Wizard user = core.getUserManager().getUser(player);

        if (utils.isTome(mainhand)) {
            String spellId = utils.getTomeSpellId(mainhand);
            int tomeLevel = utils.getTomeLevel(mainhand);

            Spell spell = core.getSpellManager().getSpell(spellId);

            if (user.hasSpellUnlocked(spellId)) {
                int spellLevel = user.getSpellLevel(spellId);

                if (spellLevel == tomeLevel) {
                    Chat.sendTitle(player, "", "&r" + spell.getSpellName(player, tomeLevel) + " &c is unlocked already", 10, 15, 10);
                    return;
                }

                if (spellLevel > tomeLevel) {
                    Chat.sendTitle(player, "&bWeak Tome", "&r" + spell.getSpellName(player, tomeLevel) + " is weaker than your spells lvl", 10, 15, 15);
                    return;
                }
            }

            user.setSpellLevel(spellId, tomeLevel);
            Chat.sendTitle(player, "&aSpell Unlocked", spell.getSpellName(player, tomeLevel), 10, 20, 10);
            PlayerUtils.removeFromHand(player, 1, HandSlot.MAIN_HAND);
            /* TODO Create a MagicTomeConsumeEvent here.*/
            return;
        }


        if (!core.getWizardItemUtils().isWand(PlayerUtils.getItemInHand(player, HandSlot.MAIN_HAND))) {

            return;
        }

        SpellBindSlot chosenSpellBind = getChosenActivation(e);

        if (chosenSpellBind == null) {
            return;
        }

        //no bind in the slot.
        if (!user.hasSpellBoundAtSlot(chosenSpellBind)) {
            Chat.actionMessage(player, "&cNo spell bound in slot &e" + chosenSpellBind.getSlotName());
            e.setCancelled(true);
            return;
        }

        //todo check mana cost & user mana amt.

        //Get the spell bound, and blast the bitty.
        Spell spellBound = user.getSpellAtSlot(chosenSpellBind);

        /* todo remove gamemode check, it's for debugging (cooldown bypass) */
        if (user.isOnCooldown(spellBound) && player.getGameMode() == GameMode.SURVIVAL) {

            Chat.actionMessage(player, String.format("&c%s is on cooldown for %s", spellBound.id(), user.getCooldownBreakdown(spellBound)));
            e.setCancelled(true);
            return;
        }

        spellBound.onCast(player);
        user.setOnCooldown(player, spellBound);
        //todo reduce mana
    }

    @EventHandler
    public void onProjectileHitEntityEvent(ParticleProjectileHitEntityEvent e) {
        ParticleProjectile projectile = e.getProjectile();


        //nope outta any unwantedness
        if (!(projectile instanceof Spell)) {
            return;
        }

        LivingEntity source = e.getSourceEntity();

        /* Player not casting the spell... todo :: later implement spells
        being cast from non player sources (blocks, mobs, etc).
         */
        if (!(source instanceof Player)) {
            return;
        }

        Player player = (Player) e.getSourceEntity();

        Spell spell = (Spell) projectile;
        spell.onHitEntity(player, e.getEntity());

        //todo check if player killed with magic by comparing entity before and after.
    }

    @EventHandler
    public void onProjectileHitBlockEvent(ParticleProjectileHitBlockEvent e) {
        if (!(e.getSourceEntity() instanceof Player)) {
            return;
        }
        Player source = (Player) e.getSourceEntity();
        Block block = e.getBlock();
        Location loc = e.getBlock().getLocation();
//        Chat.actionMessage(source, String.format("&cHit at: %sx,%sy,%sz", block.getX(), block.getY(), block.getZ()));
        ParticleProjectile projectile = e.getProjectile();
        if (!(projectile instanceof Spell)) {
            return;
        }

        ((Spell) projectile).onHitBlock(source, block);
    }
}
