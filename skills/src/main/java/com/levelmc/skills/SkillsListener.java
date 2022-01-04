package com.levelmc.skills;

import com.destroystokyo.paper.ParticleBuilder;
import com.levelmc.core.api.ApiMessages;
import com.levelmc.core.api.utils.LevelExpUtil;
import com.levelmc.core.api.utils.ProgressBarUtil;
import com.levelmc.core.chat.Chat;
import com.levelmc.core.api.inventory.HandSlot;
import com.levelmc.core.api.item.ToolType;
import com.levelmc.core.api.players.PlayerUtils;
import com.levelmc.core.api.utils.NumberUtil;
import com.levelmc.core.api.utils.PluginUtils;
import com.levelmc.skills.events.PlayerSkillGainedExpEvent;
import com.levelmc.skills.events.PlayerSkillLevelEvent;
import com.levelmc.skills.events.PlayerSkillPointGainEvent;
import com.levelmc.skills.config.MiningConfiguration;
import com.levelmc.skills.config.WoodcuttingConfiguration;
import com.levelmc.skills.events.PlayerSkillPointAssignEvent;
import com.levelmc.skills.perks.woodcutting.TreeFell;
import com.levelmc.skills.perks.woodcutting.WoodChipper;
import com.levelmc.skills.users.SkillUser;
import com.levelmc.skills.utils.DamageIndicatorUtil;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

public class SkillsListener implements Listener {

    /*
    Todo: Implement logging of player placed resource to prevent EXP Abuse.
     */

    /* todo determine if a progress bar is necessary */

    private Skills core = null;


    private MiningConfiguration miningConfig = null;

    private WoodcuttingConfiguration woodcuttingConfiguration = null;

    public SkillsListener(Skills core) {
        this.core = core;

        this.miningConfig = core.getSkillsConfig().getMiningConfig();
        this.woodcuttingConfiguration = core.getSkillsConfig().getWoodcuttingConfiguration();
    }

    protected void showActionBar(PlayerSkillGainedExpEvent e) {
        Player player = e.getPlayer();
        SkillUser user = e.getUser();
        int currentLevel = user.getLevel(e.getSkillType());
        int currentExp = user.getExperience(e.getSkillType());
        int nextLevelExp = LevelExpUtil.getExperienceAtLevel(currentLevel + 1);

        int remainingExp = nextLevelExp - currentExp; //if 83 and 50 remaining is 23
        int currentLevelExp = LevelExpUtil.getExperienceAtLevel(currentLevel); //xp to reach current lvl

        int nextLevelExpGap = nextLevelExp - currentLevelExp; //difference in exp between two levels.

        int expTowardsLevel = nextLevelExp - remainingExp;

        double expPercentLeft = ((double) expTowardsLevel / (double) nextLevelExpGap) * 100;
        int numericPercentLeft = (int) NumberUtil.round(expPercentLeft, 1);
        Chat.actionMessage(player, ProgressBarUtil.renderProgressBar("&a", "&c", ProgressBarUtil.DEFAULT_BAR, numericPercentLeft));
    }

    @EventHandler
    public void onPlayerSkillLevelUp(PlayerSkillLevelEvent e) {
        Player p = e.getPlayer();
        Chat.sendTitle(p, "&6Level Up!", String.format("&d%s &7► &dLvl &b%s", e.getSkillType().getSkillName(), e.getNewLevel()), 10, 20, 10);

        SkillUser user = e.getUser();
        if (e.getNewLevel() == 1) {
            return;
        }
        PlayerSkillPointGainEvent spGainEvent = new PlayerSkillPointGainEvent(p, e.getSkillType(), 1);
        PluginUtils.callEvent(spGainEvent);
    }

    @EventHandler
    public void onPlayerGainSkillPoint(PlayerSkillPointGainEvent e) {
        e.getUser().addSkillPoint(e.getSkillType(), e.getAmount());
        Chat.actionMessage(e.getPlayer(), String.format(ApiMessages.get("skill-point-gained"), e.getAmount(), WordUtils.capitalize(e.getSkillType().getSkillName())));
    }

    @EventHandler
    public void onPlayerSkillPointAssignEvent(PlayerSkillPointAssignEvent e) {
        e.getUser().addPerkLevel(e.getPerk().getPerkId(), e.getAmount());
        e.getUser().subtractSkillPoint(e.getSkillType(),e.getAmount());
        Chat.message(e.getPlayer(), String.format(ApiMessages.get("skill-leveled"), WordUtils.capitalize(e.getSkillType().getSkillName()), String.valueOf(e.getLevel())));
    }

    @EventHandler
    public void onPlayerGainedSkillExpEvent(PlayerSkillGainedExpEvent e) {
        SkillUser user = e.getUser();

        int previousLevel = user.getLevel(e.getSkillType());
        user.addExperience(e.getSkillType(),e.getAmount());
        int newLevel = user.getLevel(e.getSkillType());

        showActionBar(e);
        DamageIndicatorUtil.getInstance().spawnDamageIndicatorDelayed(e.getPlayer(), String.format(ApiMessages.get("skill-exp-gained"), e.getAmount(), e.getSkillType().getSkillName()), EntityDamageEvent.DamageCause.CUSTOM, e.getAmount(), 5L);

        if (previousLevel < newLevel) {
            try {
                PlayerSkillLevelEvent levelEvent = new PlayerSkillLevelEvent(e.getPlayer(), e.getSkillType(), previousLevel, newLevel);
                Bukkit.getPluginManager().callEvent(levelEvent);
            } catch (Exception ee) {
                ee.printStackTrace();
            }
        }
    }

    @EventHandler
    public void onBlockBreakEvent(BlockBreakEvent e) {
        Block block = e.getBlock();
        Material brokenType = block.getType();
        Player player = e.getPlayer();
        SkillUser user = core.getUserManager().getUser(player);
        /* Can't continue if they're bare handed */

        if (PlayerUtils.handIsEmpty(player, HandSlot.MAIN_HAND)) {
            core.getLogger().info(String.format("Player %s has an empty hand", player.getName()));
            return;
        }

        ItemStack hand = PlayerUtils.getItemInHand(player, HandSlot.MAIN_HAND);

        ToolType type = ToolType.getType(hand.getType());

        if (type == null) {
            core.getLogger().info(String.format("Unable to indentify tool type for %s", hand.getType().name()));
            return;
        }

        SkillType skillType = null;
        int rewardAmount = 0;

        switch (type) {
            case PICK_AXE:
                if (!miningConfig.hasExpReward(brokenType)) {
                    return;
                }

                skillType = SkillType.MINING;
                rewardAmount = miningConfig.getExpReward(brokenType);

                if (user.hasPerk("mining-mastery") && Skills.getInstance().getPerk("mining-mastery").activate(player)) {
                    for (ItemStack item : e.getBlock().getDrops(hand)) {
                        item.setAmount(item.getAmount() * 2);
                        new ParticleBuilder(Particle.ELECTRIC_SPARK).count(3).receivers(player).location(e.getBlock().getLocation()).spawn();
                    }
                    Chat.msg(player, "&aDouble Drops Activated");
                }
                //todo check for perks & activate
                break;
            case AXE:
                if (!woodcuttingConfiguration.hasExpReward(brokenType)) {
                    return;
                }
                skillType = SkillType.WOODCUUTTING;
                rewardAmount = woodcuttingConfiguration.getExpReward(brokenType);

                //todo check for perks and activate.

                /*
                If the player is using tree fell, it'll check first.
                 */
                if (user.hasPerk("tree-fell") && user.isPerkActive("tree-fell")) {
                    TreeFell treeFell = TreeFell.getInstance();

                    if (treeFell.activate(player)) {
                        Chat.actionMessage(player, "&a&lTree Fell Activated");
                        treeFell.onBlockBreakEvent(e);
                        return;
                    }
                }

                /*
                If the player has unlocked the wood chipper, and it's active then get to using it!

                Wood chipper should also consume the players axe durability based on its level.

                Lower levels = Higher durability hits for the wood that's fell.
                 */
                if (user.hasPerk("wood-chipper") && user.isPerkActive("wood-chipper")) {
                    WoodChipper.getInstance().handle(e);
                    //todo apply damage per use based on level.
                }
                break;
            case HOE:
            case SHOVEL:
            default:
                break;
        }

        if (skillType != null) {

            PlayerSkillGainedExpEvent event = new PlayerSkillGainedExpEvent(player, skillType, rewardAmount);
            PluginUtils.callEvent(event);
        }
    }

    @EventHandler
    public void onMobKillEvent(EntityDeathEvent e) {

    }
}
