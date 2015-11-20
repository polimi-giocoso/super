package it.playfellas.superapp.events.ui;

import lombok.Getter;

/**
 * Created by affo on 06/08/15.
 */
public class ScoreUpdateEvent extends UIEvent {
    @Getter
    private int score;

    public ScoreUpdateEvent(int score) {
        this.score = score;
    }
}
