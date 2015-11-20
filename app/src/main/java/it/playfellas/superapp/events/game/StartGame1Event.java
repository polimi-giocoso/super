package it.playfellas.superapp.events.game;

import it.playfellas.superapp.logic.Config1;
import lombok.Getter;

/**
 * Created by affo on 30/07/15.
 */
public abstract class StartGame1Event extends StartGameEvent {
    @Getter
    private Config1 conf;

    public StartGame1Event(Config1 conf) {
        this.conf = conf;
    }
}
