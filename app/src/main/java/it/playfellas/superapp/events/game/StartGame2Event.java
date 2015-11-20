package it.playfellas.superapp.events.game;

import it.playfellas.superapp.logic.Config2;
import lombok.Getter;

/**
 * Created by affo on 30/07/15.
 */
public class StartGame2Event extends StartGameEvent {
    @Getter
    private Config2 conf;

    public StartGame2Event(Config2 conf) {
        this.conf = conf;
    }
}
