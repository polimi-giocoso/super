package it.playfellas.superapp.events.game;

import it.playfellas.superapp.logic.Config3;
import lombok.Getter;

/**
 * Created by affo on 30/07/15.
 */
public class StartGame3Event extends StartGameEvent {
    @Getter
    private Config3 conf;

    public StartGame3Event(Config3 conf) {
        this.conf = conf;
    }
}
