package com.levelmc.core.api.game;

import com.levelmc.core.api.game.clause.ServerShutdownClause;
import com.levelmc.core.api.game.state.GameState;
import com.levelmc.core.api.game.state.GameStateManager;
import com.levelmc.core.api.game.state.IGameState;
import com.levelmc.core.api.game.thread.GameTickThread;
import com.levelmc.core.api.players.UserManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashSet;
import java.util.Set;

/**
 * Barebones implementation of the IGameCore.
 */
public abstract class CraftGame<T extends UserManager> extends JavaPlugin implements IGameCore {

    private Set<ServerShutdownClause> shutdownClauses = new HashSet<>();

    private GameStateManager stateManager;

    private GameTickThread tickThread = null;

    @Override
    public void onEnable() {
        /*
        Initialize configuration first.
         */
        initConfig();

        /*
        Initialize the game state manager
         */
        stateManager = new GameStateManager(this);

        //super enable
        super.onEnable();

        //This startup logic
        startup();

        //Create the core update thread and begin running it immediately, with the desired delay.
        tickThread = new GameTickThread(this);
        tickThread.runTaskTimer(this,20,tickDelay());
    }

    public abstract void startup();

    public abstract void shutdown();

    public abstract String getAuthor();

    public abstract void initConfig();

    @Override
    public void update() {
        stateManager.update();
    }

    public abstract long tickDelay();

    public abstract T getUserManager();

    public GameStateManager getStateManager() {
        return stateManager;
    }
    /**
     * Register a {@link IGameState} with the minigames engine.
     * If a game-state with the given ID already exists, it's overwritten.
     *
     * @param state state to register.
     */
    public void registerGameState(GameState state) {
        getStateManager().addGameState(state);
    }


    /**
     * Register the given {@link IGameState}(s) with the minigame engine.
     *
     * @param states states to register.
     */
    public void registerGameStates(GameState... states) {
        for (GameState state : states) {
            registerGameState(state);
        }
    }

    void debug(String... msg) {
        for(String m : msg) {
            getLogger().info(m);
        }
    }

}
