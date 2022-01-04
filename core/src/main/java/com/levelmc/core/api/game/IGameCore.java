package com.levelmc.core.api.game;

import com.levelmc.core.api.game.state.MiniGame;

/**
 * Core used to handle operations related to Game-Engines in commons.
 * Current implementations: {@link CraftGame}, {@link MiniGame}
 */
public interface IGameCore {

    /**
     * Operations to perform each game-tick.
     */
    void update();

    /**
     * Delay between each game tick, in minecraft ticks. 20 ticks = 1 second.
     * @return delay between each game tick.
     */
    long tickDelay();
}
