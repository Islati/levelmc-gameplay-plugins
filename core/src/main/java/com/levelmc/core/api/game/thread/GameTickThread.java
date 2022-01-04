package com.levelmc.core.api.game.thread;

import com.levelmc.core.api.game.CraftGame;
import com.levelmc.core.api.game.IGameCore;
import org.bukkit.scheduler.BukkitRunnable;

public class GameTickThread extends BukkitRunnable {

    private IGameCore core;

    public GameTickThread(CraftGame core) {
        this.core = core;
    }

    @Override
    public void run() {
        //Tick the main update thread!
        core.update();
    }
}
