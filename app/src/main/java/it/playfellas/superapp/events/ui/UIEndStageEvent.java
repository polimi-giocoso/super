package it.playfellas.superapp.events.ui;

import lombok.Getter;

/**
 * Created by affo on 06/08/15.
 */
public class UIEndStageEvent extends UIEvent {
    @Getter
    private int stageNumber;

    public UIEndStageEvent(int stageNumber) {
        this.stageNumber = stageNumber;
    }
}
