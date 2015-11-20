package it.playfellas.superapp.events.ui;

/**
 * Created by affo on 14/09/15.
 */
public class UIEndGameEvent extends UIEvent {
    private boolean won;

    public UIEndGameEvent(boolean won) {
        this.won = won;
    }

    public boolean won() {
        return this.won;
    }
}
