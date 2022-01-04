package com.levelmc.core.api.thread;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class BukkitFuture {

    /*
    Cache the local variables for an hour and expire if they're not being used.
     */
    private static Cache<String, BukkitFuture> futureCache = CacheBuilder.newBuilder()
            .expireAfterWrite(1, TimeUnit.HOURS)
            .build();


    /**
     * Retrieve (or create & cache) a BukkitFuture instance specific to the plugin.
     * @param plugin parent plugin which is executing the tatsks
     * @return BukkitFuture bound to parent plugin
     */
    public static BukkitFuture get(Plugin plugin) {
        if (!futureCache.asMap().containsKey(plugin.getName())) {
            futureCache.put(plugin.getName(), new BukkitFuture(plugin));
        }

        return futureCache.asMap().get(plugin.getName());
    }

    private Plugin plugin;

    protected BukkitFuture(Plugin plugin) {
        this.plugin = plugin;
    }


    /**
     * Returns a new CompletableFuture that is asynchronously completed by Bukkit schedule with the value obtained by calling the given Supplier.
     *
     * @param supplier a function returning the value to be used to complete the returned CompletableFuture
     * @param <T>      the function's return type
     */
    public <T> CompletableFuture<T> supplyAsync(@NonNull Supplier<T> supplier) {
        CompletableFuture<T> future = new CompletableFuture<>();
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                future.complete(supplier.get());
            } catch (Throwable t) {
                future.completeExceptionally(t);
            }
        });
        return future;
    }

    /**
     * Returns a new CompletableFuture that is asynchronously completed by Bukkit schedule after it runs the given action.
     *
     * @param runnable the action to run before completing the returned CompletableFuture
     */
    public CompletableFuture<Void> runAsync(@NonNull Runnable runnable) {
        CompletableFuture<Void> future = new CompletableFuture<>();
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                runnable.run();
                future.complete(null);
            } catch (Throwable t) {
                future.completeExceptionally(t);
            }
        });
        return future;
    }

    /**
     * Returns a new CompletableFuture that is synchronously completed by Bukkit schedule with the value obtained by calling the given Supplier.
     *
     * @param supplier a function returning the value to be used to complete the returned CompletableFuture
     * @param <T>      the function's return type
     */
    public <T> CompletableFuture<T> supplySync(@NonNull Supplier<T> supplier) {
        CompletableFuture<T> future = new CompletableFuture<>();
        if (Bukkit.isPrimaryThread()) {
            try {
                future.complete(supplier.get());
            } catch (Throwable t) {
                future.completeExceptionally(t);
            }
        } else {
            Bukkit.getScheduler().runTask(plugin, () -> {
                try {
                    future.complete(supplier.get());
                } catch (Throwable t) {
                    future.completeExceptionally(t);
                }
            });
        }
        return future;
    }

    /**
     * Returns a new CompletableFuture that is synchronously completed by Bukkit schedule after it runs the given action.
     *
     * @param runnable the action to run before completing the returned CompletableFuture
     */
    public CompletableFuture<Void> runSync(@NonNull Runnable runnable) {
        CompletableFuture<Void> future = new CompletableFuture<>();
        if (Bukkit.isPrimaryThread()) {
            try {
                runnable.run();
                future.complete(null);
            } catch (Throwable t) {
                future.completeExceptionally(t);
            }
        } else {
            Bukkit.getScheduler().runTask(plugin, () -> {
                try {
                    runnable.run();
                    future.complete(null);
                } catch (Throwable t) {
                    future.completeExceptionally(t);
                }
            });
        }
        return future;
    }

    /**
     * Helper method to avoid boilerplate in CompletableFuture#whenComplete .
     *
     * @param action the BiConsumer of the whenComplete method that will be executed in the runnable synchronously.
     * @param <T>    the function's return type
     */
    public <T> BiConsumer<T, ? super Throwable> sync(@NonNull BiConsumer<T, ? super Throwable> action) {
        return (BiConsumer<T, Throwable>) (t, throwable) -> runSync(() -> action.accept(t, throwable));
    }
}
