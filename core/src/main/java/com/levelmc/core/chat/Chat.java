package com.levelmc.core.chat;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.Collection;

/**
 * Used to messages to players, console, specific permission groups, users with permissions, action messages, titles, and more.
 */
public class Chat {
    private static ConsoleCommandSender console = Bukkit.getConsoleSender();

    public static String strip(String msg) {
        return ChatColor.stripColor(msg);
    }

    public static String colorize(String msg) {
        return ChatColor.translateAlternateColorCodes('&', msg);
    }


    /**
     * Broadcast the messages passed, automagically formatting all color codes.
     *
     * @param messages message(s) to be broadcasted.
     */
    public static void broadcast(String... messages) {
        for (String message : messages) {
            Bukkit.broadcastMessage(colorize(message));
        }
    }

    /**
     * Broadcast the message passed (formatting all color codes) to all players
     * action bars!
     *
     * @param message message to send to players action bars.
     */
    public static void broadcastActionMessage(String message) {
        Bukkit.getOnlinePlayers().stream().forEach(p -> actionMessage(p, message));
    }

    /**
     * Message the console,and have any color codes colorized.
     *
     * @param messages messages to send to the console.
     */
    public static void messageConsole(String... messages) {
        String[] msgs = messages;
        for (int i = 0; i < msgs.length; i++) {
            msgs[i] = colorize(msgs[i]);
        }

        console.sendMessage(msgs);
    }

    /**
     * Message the console a collection of messages, translating all color codes.
     *
     * @param messages messages to send to the console.
     */
    public static void messageConsole(Collection<String> messages) {
        for (String message : messages) {
            console.sendMessage(colorize(message));
        }
    }

    /**
     * Send the player a message on their action-bar, as opposed to the chat window.
     *
     * @param player  player receiving the message.
     * @param message message to send.
     */
    public static void actionMessage(Player player, String message) {
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR,new TextComponent(colorize(message)));
    }

    public static void message(CommandSender sender, String... msg) {
        msg(sender, msg);
    }

    public static void message(CommandSender sender, BaseComponent... components) {
        sender.spigot().sendMessage(components);
    }

    public static void messageAll(Collection<CommandSender> receivers, String... msg) {
        if (receivers == null || receivers.isEmpty()) {
            return;
        }

        for (CommandSender sender : receivers) {
            message(sender, msg);
        }
    }

    public static void msg(CommandSender sender, BaseComponent... components) {
        sender.spigot().sendMessage(components);
    }


    public static void msg(CommandSender sender, String... msg) {
        for (String m : msg) {
            sender.sendMessage(colorize(m));
        }
    }

    public static void format(CommandSender p, String text, Object... args) {
        p.sendMessage(colorize(String.format(text, args)));
    }

    public static void sendTitle(Player player, String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        player.sendTitle(colorize(title),colorize(subtitle),fadeIn,stay,fadeOut);
    }

}
