package com.zain.game;

/**
 * A class that keeps track of time deltas between calls to its progress() method.
 */
public class ElapsedTimer {
    private long updateStartTime = 0L;

    private boolean initialized = false;

    public long getUpdateStartTime() {
        return updateStartTime;
    }
    public boolean getInitialized() {
        return initialized;
    }

    public void setInitialized(boolean initialized) {
        this.initialized = initialized;
    }

    public long progress() {
        final long now = System.currentTimeMillis();
        if (!initialized) {
            initialized = true;
            updateStartTime = now;
        }
        final long delta = now - updateStartTime;
       // updateStartTime = now;
        return delta;
    }
}

