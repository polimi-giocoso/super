package it.playfellas.superapp.logic.master;

import android.bluetooth.BluetoothDevice;
import android.util.Log;

import com.firebase.client.Firebase;
import com.squareup.otto.Subscribe;

import java.util.Timer;
import java.util.TimerTask;

import it.playfellas.superapp.events.EventFactory;
import it.playfellas.superapp.events.game.RWEvent;
import it.playfellas.superapp.events.game.StartGameEvent;
import it.playfellas.superapp.logic.Config;
import it.playfellas.superapp.network.TenBus;
import lombok.Getter;

/**
 * Created by affo on 28/07/15.
 */
public abstract class MasterController {
    private static final String TAG = MasterController.class.getSimpleName();
    private static final String FIREBASE_URL = "https://giocoso2015.firebaseio.com/";
    private Firebase fbRef;
    private Config conf;

    private float currentRtt;
    private Timer rttDownCounter;
    private int score;
    private int stage;
    private boolean stageRunning;
    @Getter
    private boolean gameRunning;
    private GameHistory history;

    // Object to be registered on `TenBus`.
    // We need it to make extending classes inherit
    // `@Subscribe` methods.
    private Object busListener;

    public MasterController(final Config conf) {
        super();
        this.conf = conf;
        score = 0;
        stage = 0;
        stageRunning = false;
        gameRunning = false;

        fbRef = new Firebase(FIREBASE_URL);
        history = new GameHistory(fbRef);
        rttDownCounter = new Timer(true);

        busListener = new Object() {
            @Subscribe
            public void onRw(RWEvent e) {
                synchronized (MasterController.this) {
                    String player = e.deviceName;
                    boolean rw = e.isRight();

                    onAnswer(rw);
                    notifyScore();

                    if (rw) {
                        history.right(player);
                    } else {
                        history.wrong(player);
                    }

                    // not >=, we want to fire endStage only once!
                    if (score == conf.getMaxScore()) {
                        // you win!
                        endStage();
                        return;
                    }
                }
            }
        };
        TenBus.get().register(busListener);
    }

    /**
     * This method destroys the `MasterController`.
     * After a call to this method the object becomes
     * unusable (e.g. it does not respond to Otto events
     * anymore).
     * If you extend this class, please remember to override
     * this method, call super.destroy() and perform
     * your "destroy" tasks.
     */
    public void destroy() {
        destroyRttTimer();
        rttDownCounter = null;
        TenBus.get().unregister(busListener);
    }

    public String getGameID() {
        return this.history.getGameID();
    }

    /**
     * Called exactly after slaves are notified
     * with a `BeginStageEvent`.
     */
    protected abstract void onBeginStage();

    /**
     * Called exactly after slaves are notified
     * with a `EndStageEvent`.
     */
    protected abstract void onEndStage();

    /**
     * Called every time an answer is given
     * (i.e. when a tile is clicked by a slave).
     * Typical actions could be updating the `score` using provided methods.
     *
     * @param rw boolean, true if answer is right, false otherwise.
     */
    protected abstract void onAnswer(boolean rw);

    /**
     * @return The corresponding `StartGameEvent` (i.e. 1/2/3) to this `MasterController`
     */
    protected abstract StartGameEvent getNewGameEvent();

    protected synchronized void notifyScore() {
        TenBus.get().post(EventFactory.scoreUpdate(score));
    }

    protected synchronized void setScore(int score) {
        this.score = score;
    }

    protected synchronized void incrementScore() {
        if (getScore() + 1 <= conf.getMaxScore()) {
            this.score++;
        }
    }

    protected synchronized void decrementScore() {
        if (getScore() - 1 >= 0) {
            this.score--;
        }
    }

    protected synchronized void resetScore() {
        this.score = 0;
    }

    protected BluetoothDevice nextPlayer() {
        return TenBus.get().nextDevice();
    }

    /**
     * Call from presenter
     */
    public void beginStage() {
        if (stageRunning) {
            Log.w(TAG, "Cannot begin stage while stage is running. Returning silently...");
            return;
        }

        if (stage == 0) {
            gameRunning = true;
            TenBus.get().post(getNewGameEvent());
        }

        resetRtt();
        resetScore();
        notifyScore();

        if (conf.isSpeedUp()) {
            long updatePeriod = (long) (conf.getRttUpdatePeriod() * 1000); // from s to ms
            // scheduling `decreaseRtt` after `rttPeriod`
            // with period `rttPeriod`.
            rttDownCounter.schedule(new TimerTask() {
                @Override
                public void run() {
                    decreaseRtt();
                }
            }, updatePeriod, updatePeriod);
        }

        TenBus.get().post(EventFactory.beginStage());
        TenBus.get().post(EventFactory.uiBeginStage(stage));
        stageRunning = true;

        onBeginStage();
    }

    public void endGame() {
        gameRunning = false;
        destroyRttTimer();
        TenBus.get().post(EventFactory.endGame(false));
        TenBus.get().post(EventFactory.uiEndGame(false));
    }

    public synchronized int getScore() {
        return score;
    }

    public synchronized int getStage() {
        return stage;
    }

    private void endStage() {
        if (!stageRunning) {
            Log.w(TAG, "Cannot end stage while stage is not running. Returning silently...");
            return;
        }
        destroyRttTimer();

        TenBus.get().post(EventFactory.endStage());
        TenBus.get().post(EventFactory.uiEndStage(stage));
        stageRunning = false;

        history.endStage();
        onEndStage();

        stage++;
        if (stage >= conf.getNoStages()) {
            gameRunning = false;
            TenBus.get().post(EventFactory.endGame(true));
            TenBus.get().post(EventFactory.uiEndGame(true));
            history.save();
        }
    }

    private void destroyRttTimer() {
        rttDownCounter.cancel();
        rttDownCounter.purge();
        rttDownCounter = new Timer(true);
    }

    private void resetRtt() {
        currentRtt = conf.getDefaultRtt();
        notifyRtt(currentRtt);
    }

    private void decreaseRtt() {
        if (currentRtt <= conf.getMinRtt()) {
            rttDownCounter.cancel();
            return;
        }

        currentRtt -= conf.getRttDecreaseDelta();
        notifyRtt(currentRtt);
    }

    private void notifyRtt(float rtt) {
        TenBus.get().post(EventFactory.rttUpdate(rtt));
    }
}
