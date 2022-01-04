package com.levelmc.core.api.game.state;

public abstract class GameState implements IGameState {

    /* Used to track whether or not listeners have been registered for this game state */
    private boolean listenersRegistered = false;

    private boolean setup = false;

    private int id;

    private MiniGame parent = null;

    public GameState(MiniGame parent, int id) {
        this.parent = parent;
        this.id = id;
    }

    public abstract void update();

    public abstract boolean switchState();

    public abstract int nextState();

    public void setSetup(boolean val) {
        setup = val;
    }

    @Override
    public boolean isSetup() {
        return setup;
    }

    @Override
    public abstract void setup();

    public abstract void destroy();

    public GameState() {}

    public GameState(int id) {
        this.id = id;
    }

    public void debug(String... text) {
    }

    public int id() {
        return id;
    }

    protected void setId(int id) {
        this.id = id;
    }

    public boolean hasListenersRegistered() {
        return listenersRegistered;
    }

    public void setListenersRegistered(boolean registered) {
        listenersRegistered = registered;
    }
}
