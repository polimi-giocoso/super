package it.playfellas.superapp.events.game;

import it.playfellas.superapp.events.NetEvent;
import lombok.Getter;

/**
 * Created by affo on 02/09/15.
 */
public class PopEvent extends NetEvent {
    @Getter
    private boolean wrongMove;

    public PopEvent(boolean wrongMove) {
        this.wrongMove = wrongMove;
    }
}
