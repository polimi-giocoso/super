package it.playfellas.superapp.ui.slave;

import com.squareup.otto.Subscribe;

import it.playfellas.superapp.events.game.BeginStageEvent;
import it.playfellas.superapp.events.game.EndGameEvent;
import it.playfellas.superapp.events.game.EndStageEvent;
import it.playfellas.superapp.events.game.RTTUpdateEvent;
import it.playfellas.superapp.events.tile.NewTileEvent;
import it.playfellas.superapp.events.tile.NewTutorialTileEvent;
import it.playfellas.superapp.events.ui.UIRWEvent;
import it.playfellas.superapp.network.TenBus;

/**
 * Created by Stefano Cappa on 07/08/15.
 */
public abstract class SlavePresenter {

    /**
     * Object to be registered on {@link it.playfellas.superapp.network.TenBus}.
     * We need it to make extending classes inherit @Subscribe methods.
     */
    private Object busListener;

    public SlavePresenter() {
        busListener = new Object() {
            @Subscribe
            public void onUIRWEvent(UIRWEvent e) {
                getSlaveGameFragment().onRightOrWrong(e);
            }

            @Subscribe
            public void onNewTileEvent(NewTileEvent event) {
                newTileEvent(event);
            }

            @Subscribe
            public void onNewTutorialTile(NewTutorialTileEvent event) {
                newTileEvent(event);
            }

            @Subscribe
            public void onBeginStageEvent(BeginStageEvent event) {
                beginStageEvent(event);
            }

            @Subscribe
            public void onEndStageEvent(EndStageEvent event) {
                endStageEvent(event);
            }

            @Subscribe
            public void onEndGameEvent(EndGameEvent event) {
                endGameEvent(event);
            }

            @Subscribe
            public void onRttEvent(RTTUpdateEvent event) {
                rttUpdateEvent(event);
            }
        };
        TenBus.get().register(busListener);
    }

    private void unregisterTenBusObject() {
        TenBus.get().unregister(busListener);
    }

    protected abstract void startConveyors();

    protected abstract void clearConveyors();

    protected abstract void stopConveyors();

    protected abstract void newTileEvent(NewTileEvent event);

    protected abstract void newTileEvent(NewTutorialTileEvent event);

    protected abstract SlaveGameFragment getSlaveGameFragment();

    protected abstract void beginStageEvent(BeginStageEvent event);

    protected abstract void endStageEvent(EndStageEvent event);

    protected abstract void endGameEvent(EndGameEvent event);

    protected abstract void rttUpdateEvent(RTTUpdateEvent event);

    public abstract void start();

    public abstract void pause();

    public void kill() {
        this.unregisterTenBusObject();
    }
}
