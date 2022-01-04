package com.levelmc.core.api.game.state;

import com.levelmc.core.api.game.CraftGame;
import com.levelmc.core.api.game.clause.ServerShutdownClause;
import com.levelmc.core.api.players.UserManager;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * An extension of {@link CraftGame} used to easily create MiniGame-based plugins without all the boiler-plating required when normally setting them up.
 *
 */
public abstract class MiniGame extends CraftGame {

	private Set<ServerShutdownClause> shutdownClauses = new HashSet<>();

	private boolean gameOver = false;

	private boolean autoSave = true;

	/**
	 * Initialize the minigame without direct registration of any classes
	 */
	public MiniGame() {

	}

	@Override
	public void onEnable() {
		super.onEnable();
	}

	@Override
	public void onDisable() {
		super.onDisable();

		shutdown();
	}

	@Override
	public void update() {
		super.update();

		if (!shutdownClauses.isEmpty() && doShutdown()) {
			onDisable();
		}
	}

	/**
	 * Check if the server is to shutdown.
	 *
	 * @return Whether or not to shutdown. If any of the conditions are met where the server is to shut down, true is returned, otherwise false.
	 */
	protected boolean doShutdown() {
		for (ServerShutdownClause clause : shutdownClauses) {
			if (clause.shutdown()) {
				return true;
			}
		}
		return false;
	}


	/**
	 * Register {@link ServerShutdownClause}'s, that if passed will force the server to stop.
	 *
	 * @param clauses clauses to register.
	 */
	public void registerShutdownClauses(ServerShutdownClause... clauses) {
		Collections.addAll(shutdownClauses, clauses);
	}

	/**
	 * @return whether or not the game is over.
	 */
	public boolean isGameOver() {
		return gameOver;
	}

	/**
	 * Set whether or not the game is over.
	 *
	 * @param gameOver value to assign.
	 */
	public void setGameOver(boolean gameOver) {
		this.gameOver = gameOver;
	}

	/**
	 * @return whether or not auto-save data is enabled.
	 */
	public boolean autoSave() {
		return autoSave;
	}

	/**
	 * Change whether or not to automatically save data.
	 *
	 * @param autoSave value to assign.
	 */
	public void setAutoSave(boolean autoSave) {
		this.autoSave = autoSave;
	}

	/**
	 * Operations to perform when the MiniGame is being initialized.
	 */
	public abstract void startup();

	/**
	 * Operations to perform when the MiniGame is shutting down.
	 */
	public abstract void shutdown();

	/**
	 * Retrieve the author of the MiniGame
	 *
	 * @return author of the MiniGame
	 */
	public abstract String getAuthor();

	/**
	 * Initialize configuration in this method. Called before {@link MiniGame#startup()}
	 */
	public abstract void initConfig();
}
