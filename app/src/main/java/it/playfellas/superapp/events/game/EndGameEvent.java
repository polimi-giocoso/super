package it.playfellas.superapp.events.game;

import it.playfellas.superapp.events.NetEvent;

/**
 * Created by affo on 29/07/15.
 */
public class EndGameEvent extends NetEvent {
    private boolean won;

    public EndGameEvent(boolean won) {
        this.won = won;
    }

    public boolean haveIWon() {
        return won;
    }
}
