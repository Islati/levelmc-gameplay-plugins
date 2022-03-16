package com.levelmc.skreet.gangs.menus;

import com.levelmc.core.api.gui.*;
import com.levelmc.core.api.item.ItemBuilder;
import com.levelmc.core.chat.Chat;
import com.levelmc.skreet.Skreet;
import com.levelmc.skreet.gangs.Gang;
import com.levelmc.skreet.gangs.GangType;
import com.levelmc.skreet.users.SkreetPlayer;
import com.levelmc.skreet.users.SkreetPlayers;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class GangSelectionMenu extends ItemMenu {

    class GangSelectionBehaviour implements MenuItemClickHandler {

        @Getter
        @Setter
        private GangType type;

        public GangSelectionBehaviour(GangType type) {
            this.type = type;
        }

        @Override
        public void onClick(MenuItem item, Player player, ClickType type) {
            SkreetPlayer user = SkreetPlayers.getInstance().getUser(player);
            Gang gang = Skreet.getInstance().getGangManager().getGang(this.type);

            if (user.hasGang()) {
                Chat.message(player, "&cYou already have a gang chosen.");
                return;
            } else if (!gang.addMember(user)) {
                Chat.message(player, "&cUnable to join gang");
                return;
            }
            item.getMenu().closeMenu(player);
        }
    }

    private static GangSelectionMenu instance = null;

    public static GangSelectionMenu getInstance() {
        if (instance == null) {
            instance = new GangSelectionMenu();
        }

        return instance;
    }

    protected GangSelectionMenu() {
        super("Choose your Gang", 1);

        item(0, new MenuItem(ItemBuilder.of(Material.RED_DYE).name("&cBlood").lore("&7On Blood").item()).onClickHandler(new GangSelectionBehaviour(GangType.BLOODS)));
        item(2, new MenuItem(ItemBuilder.of(Material.GREEN_DYE).name("&aPagan").lore("&7Pagan").item()).onClickHandler(new GangSelectionBehaviour(GangType.PAGANS)));
        item(5, new MenuItem(ItemBuilder.of(Material.BLACK_DYE).name("&0Baccas").lore("&7Of Baccas").item()).onClickHandler(new GangSelectionBehaviour(GangType.BACCAS)));
        item(7, new MenuItem(ItemBuilder.of(Material.ORANGE_DYE).name("&eAngel").lore("&eAngel").item()).onClickHandler(new GangSelectionBehaviour(GangType.ANGELS)));
        item(9, new MenuItem(ItemBuilder.of(Material.BLUE_DYE).name("&bCrip").lore("&7On Crip").item()).onClickHandler(new GangSelectionBehaviour(GangType.CRIPS)));

        exitOnClickOutside(false);
        onClose(new MenuBehaviour() {
            @Override
            public void doAction(Menu menu, Player player) {
                SkreetPlayer user = SkreetPlayers.getInstance().getUser(player);

                if (user == null) {
                    return;
                }

                if (!user.hasGang()) {
                    Menus.switchMenu(player, menu, GangSelectionMenu.getInstance());
                }
            }
        });
    }
}
