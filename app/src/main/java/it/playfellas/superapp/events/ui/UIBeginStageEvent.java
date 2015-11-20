package it.playfellas.superapp.events.ui;

import lombok.Getter;

/**
 * Created by affo on 06/08/15.
 */
public class UIBeginStageEvent extends UIEvent {
    @Getter
    private int stageNumber;

    public UIBeginStageEvent(int stageNumber) {
        this.stageNumber = stageNumber;
    }
}
