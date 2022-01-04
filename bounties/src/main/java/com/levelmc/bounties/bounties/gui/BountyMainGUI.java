package com.levelmc.bounties.bounties.gui;

import com.google.common.collect.Lists;
import com.levelmc.bounties.Bounties;
import com.levelmc.core.api.gui.*;
import com.levelmc.core.api.item.ItemBuilder;
import com.levelmc.core.api.item.SkullCreator;
import com.levelmc.bounties.bounties.Bounty;
import com.levelmc.bounties.bounties.BountyManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class BountyMainGUI extends ItemMenu {

    public static class BountyMenuItem extends MenuItem {

        public BountyMenuItem(Bounty bounty) {
            super(ItemBuilder.clone(SkullCreator.itemFromUuid(bounty.getTargetUuid())).lore("&e$" + bounty.getRewardMoney(), "&7Posted By: &a" + bounty.getPosterName()).item());

            setClickHandler(new MenuItemClickHandler() {
                @Override
                public void onClick(MenuItem item, Player player, ClickType type) {
                    
                }
            });
        }
    }

    public static class BountyMenuPageItem extends MenuItem {

        public BountyMenuPageItem(boolean next, int page) {
            super(ItemBuilder.of(Material.PAPER).name(next ? "&aNext" : "&ePrevious").lore("&7Click for page " + (page + 1)).item());

            setClickHandler(new MenuItemClickHandler() {
                @Override
                public void onClick(MenuItem item, Player player, ClickType type) {
                    item.getMenu().switchMenu(player, new BountyMainGUI(Bounties.getInstance().getBountyManager(), page));
                }
            });
        }


    }

    private static final int lastPageIndex = 48;

    private static final int nextPageIndex = 50;


    private List<Integer> filledSlots = Arrays.asList(
            0, 1, 2, 3, 4, 5, 6, 7, 8,
            9, 17,
            18, 26,
            27, 28, 34, 35,
            36, 37, 43, 44,
            45, 46, 47, 48, 49, 50, 51, 52, 53
    );

    private static final int INV_SIZE = 54;

    public BountyMainGUI(BountyManager manager, int page) {
        super("&cBounties", 6);


        List<Integer> openSlots = new ArrayList<>();
        /* Fill the empty slots */
        for (int i = 0; i < 6 * 9; i++) {
            if (i == lastPageIndex || i == nextPageIndex) {
                continue;
            }

            if (filledSlots.contains(i)) {
                item(i, MenuItem.GRAY_FILLER);
                continue;
            }
            openSlots.add(i);
        }

        if (!manager.hasBounties()) {
            return;
        }

        int openSlotsOnPage = INV_SIZE - filledSlots.size();

        List<List<Bounty>> bountiesPaged = Lists.partition(manager.getAllBounties(), openSlotsOnPage);
        List<Bounty> activeBountiesPage = null;
        try {
            activeBountiesPage = bountiesPaged.get(page - 1);
        } catch (Exception e) {
            e.printStackTrace();
            activeBountiesPage = bountiesPaged.get(0);
        }

        Iterator<Integer> slotSelector = openSlots.iterator();

        for (Bounty bounty : activeBountiesPage) {
            if (!bounty.isTargetOnline()) {
                continue;
            }
            item(slotSelector.next(), new BountyMenuItem(bounty));
        }


        if (page != 0 && bountiesPaged.size() > 1) {
            if (page > 0) {
                item(lastPageIndex, new BountyMenuPageItem(false, page - 1));
            }

            if (page < bountiesPaged.size()) {
                item(nextPageIndex, new BountyMenuPageItem(true, page + 1));
            }
        }
    }
}
