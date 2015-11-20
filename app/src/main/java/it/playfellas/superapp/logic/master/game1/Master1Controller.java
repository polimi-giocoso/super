package it.playfellas.superapp.logic.master.game1;

import it.playfellas.superapp.events.EventFactory;
import it.playfellas.superapp.logic.Config1;
import it.playfellas.superapp.logic.master.MasterController;
import it.playfellas.superapp.network.TenBus;

/**
 * Created by affo on 31/07/15.
 */
public abstract class Master1Controller extends MasterController {
    private Config1 conf;

    public Master1Controller(Config1 conf) {
        super(conf);
        this.conf = conf;
    }

    @Override
    protected void onAnswer(boolean rw) {
        if (rw) {
            incrementScore();
        } else {
            resetScore();
        }

        int score = getScore();
        if (score != 0
                && score < conf.getMaxScore()
                && score % conf.getRuleChange() == 0) {
            TenBus.get().post(EventFactory.gameChange());
            TenBus.get().post(EventFactory.uiToggleGameMode());
        }
    }
}
