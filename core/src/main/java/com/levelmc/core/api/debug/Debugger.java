package com.levelmc.core.api.debug;

import com.levelmc.core.LevelCore;
import com.levelmc.core.chat.Chat;
import com.levelmc.core.api.players.User;
import com.levelmc.core.user.LcUser;
import com.levelmc.core.user.LcUserManager;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeMap;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Handles all debug actions.
 */
public class Debugger {
    private static Debugger instance = null;

    private Set<UUID> usersDebugging = new HashSet<>();

    public static Debugger get() {
        if (instance == null) {
            instance = new Debugger();
        }

        return instance;
    }

    @Getter
    private TreeMap<String, DebugAction> debugActions = new TreeMap<>();

    protected Debugger() {

    }

    public void registerDebugActions(DebugAction... actions) {
        for (DebugAction action : actions) {
            debugActions.put(action.getId(), action);
            action.onEnable();
            LevelCore.getInstance().getLogger().info(String.format("Registered debug action %s", action.getId()));
        }
    }

    public boolean isDebugging(Player player) {
        LcUserManager userManager = LevelCore.getInstance().getUserManager();
        if (!userManager.hasData(player)) {
            return false;
        }

        LcUser user = userManager.getUser(player);
        return user.isDebugging();
    }

    public Set<CommandSender> getAllPlayersDebugging() {
        return LevelCore.getInstance().getUserManager().allUsers().stream().filter(LcUser::isDebugging).map(user -> (CommandSender)user.getPlayer()).collect(Collectors.toSet());
    }

    public void log(String... msg) {
        Chat.messageAll(getAllPlayersDebugging(), msg);
        Chat.msg(Bukkit.getConsoleSender(),msg);
        for(String m : msg) {
            LevelCore.getInstance().getLogger().info(m);
        }
    }
}
