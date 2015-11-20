package it.playfellas.superapp.logic;

import java.io.Serializable;

import it.playfellas.superapp.InternalConfig;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by affo on 30/07/15.
 */
public abstract class Config implements Serializable {
    private static final int decreaseSteps = InternalConfig.DECREASE_STEPS;
    @Getter
    private static final int rttUpdatePeriod = InternalConfig.RTT_UPDATE_PERIOD;
    private static final float baseMaxRtt = InternalConfig.BASE_MAX_RTT;
    private static final float baseMinRtt = InternalConfig.BASE_MIN_RTT;

    @Getter
    @Setter
    private boolean tutorialMode;
    @Getter
    @Setter
    private int difficultyLevel;
    @Getter
    @Setter
    private int tileDensity;
    @Getter
    @Setter
    private int maxScore;
    @Getter
    @Setter
    private int noStages;
    @Getter
    @Setter
    private boolean speedUp;

    public float getDefaultRtt() {
        return baseMaxRtt - (float) difficultyLevel / 2;
    }

    public float getMinRtt() {
        return baseMinRtt - (float) difficultyLevel / 4;
    }

    public float getRttDecreaseDelta() {
        return Math.abs(getDefaultRtt() - getMinRtt()) / decreaseSteps;
    }
}
