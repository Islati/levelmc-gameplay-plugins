package com.levelmc.core.api.thread;

import org.bukkit.scheduler.BukkitRunnable;

public abstract class IterationLimitedRunnable extends BukkitRunnable {

    private int maxIterations = 0;

    private int ranIterations = 0;

    public IterationLimitedRunnable(int maxIterations) {
        this.maxIterations = maxIterations;
    }

    @Override
    public void run() {
        if (ranIterations >= maxIterations) {
            onCancel();
            cancel();
            return;
        }

        onRun();

        ranIterations += 1;
    }

    public abstract void onRun();

    public void onCancel() {

    }


}
