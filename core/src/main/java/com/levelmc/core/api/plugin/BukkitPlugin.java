package com.levelmc.core.api.plugin;

import com.levelmc.core.api.gadgets.Gadget;
import com.levelmc.core.api.gadgets.Gadgets;
import com.levelmc.core.api.utils.PluginUtils;
import com.levelmc.core.cmd.CommandHandler;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.JavaPluginLoader;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public abstract class BukkitPlugin extends JavaPlugin {
    @Getter
    private CommandHandler commandHandler = null;

    @Getter
    @Setter
    private boolean registerGadgetListener = false;

    public BukkitPlugin() {
        super();
    }

    protected BukkitPlugin(@NotNull JavaPluginLoader loader, @NotNull PluginDescriptionFile description, @NotNull File dataFolder, @NotNull File file) {
        super(loader, description, dataFolder, file);
    }

    @Override
    public void onDisable() {
        super.onDisable();
        teardown();
    }

    @Override
    public void onEnable() {
        super.onEnable();

        if (isRegisterGadgetListener()) {
            Gadgets.init(this);
        }

        setup();
    }

    public void registerGadgets(Gadget... gadgets) {
        for (Gadget g : gadgets) {
            Gadgets.registerGadget(this, g);
        }
    }

    public void registerEventListeners(Listener... eventListeners) {
        PluginUtils.registerListeners(this, eventListeners);
    }

    public abstract void setup();

    public abstract void teardown();
}
