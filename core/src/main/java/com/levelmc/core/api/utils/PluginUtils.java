package com.levelmc.core.api.utils;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;

public class PluginUtils {
    public enum ExtractWhen {ALWAYS, IF_NOT_EXISTS, IF_NEWER}

    public static final Charset TARGET_ENCODING = Charset.forName("UTF-8");
    public static final Charset SOURCE_ENCODING = Charset.forName("UTF-8");

    private static PluginManager pluginManager = Bukkit.getPluginManager();

    public static boolean isEnabled(String name) {
        return pluginManager.isPluginEnabled(name);
    }

    public static void disablePlugin(Plugin plugin) {
        pluginManager.disablePlugin(plugin);
    }

    public static boolean enablePlugin(String name) {
        Plugin plugin = pluginManager.getPlugin(name);
        if (plugin == null || pluginManager.isPluginEnabled(name)) {
            return false;
        }
        pluginManager.enablePlugin(plugin);
        return true;
    }

    public static Plugin getPlugin(String name) {
        return pluginManager.getPlugin(name);
    }

    public static Plugin[] getPlugins() {
        return pluginManager.getPlugins();
    }

    public static boolean hasDataFolder(Plugin plugin) {
        return plugin.getDataFolder().exists();
    }

    public static boolean makeDataFolder(Plugin plugin) {
        return hasDataFolder(plugin) || plugin.getDataFolder().mkdirs();
    }

    public static void unregisterHooks(Plugin plugin) {
        Server server = plugin.getServer();
        server.getScheduler().cancelTasks(plugin);
        HandlerList.unregisterAll(plugin);
    }

    public static File getJarFile(Plugin plugin) {
        URL url = plugin.getClass().getProtectionDomain().getCodeSource().getLocation();
        try {
            return new File(url.toURI());
        } catch (URISyntaxException e) {
            return null;
        }
    }

    public static void registerListeners(Plugin plugin, Listener... listeners) {
        for (Listener listener : listeners) {
            Bukkit.getPluginManager().registerEvents(listener, plugin);
        }
    }

    public static void callEvent(Event e) {
        Bukkit.getPluginManager().callEvent(e);
    }

    public static String getBukkitVersion() {
        String name = Bukkit.getServer().getClass().getPackage().getName();
        String version = name.substring(name.lastIndexOf('.') + 1);
        return version;
    }

    public static String getNmsVersion() {
        return Bukkit.getServer().getClass().getPackage().getName().substring(23);
    }
}
