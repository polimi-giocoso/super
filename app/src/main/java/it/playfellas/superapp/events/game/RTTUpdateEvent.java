package it.playfellas.superapp.events.game;

import it.playfellas.superapp.events.NetEvent;
import lombok.Getter;

/**
 * Created by affo on 28/07/15.
 */
public class RTTUpdateEvent extends NetEvent {
    @Getter
    private float rtt;

    public RTTUpdateEvent(float rtt) {
        super();
        this.rtt = rtt;
    }
}
