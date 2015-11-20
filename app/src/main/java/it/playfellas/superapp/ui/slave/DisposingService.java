package it.playfellas.superapp.ui.slave;

import android.util.Log;

import com.squareup.otto.Subscribe;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import it.playfellas.superapp.InternalConfig;
import it.playfellas.superapp.events.EventFactory;
import it.playfellas.superapp.events.game.RTTUpdateEvent;
import it.playfellas.superapp.logic.Config;
import it.playfellas.superapp.logic.slave.SlaveController;
import it.playfellas.superapp.network.TenBus;
import it.playfellas.superapp.tiles.Tile;

/**
 * Created by affo on 15/09/15.
 * This class is meant to make the Disposing Service a singleton.
 * Every time anyone needs `Tile`s to be disposed, the only thing
 * to do is to call `DisposingService.start(...)`. If a disposer
 * has already been started it will be stopped and dereferenced and
 * a new one will be created and started.
 * Calling `DisposingService.stop()` will destroy the current disposer
 * (if any is running).
 * You cannot "pause" and "restart" a disposer. Disposers are lightweight
 * tasks, so simply `start(...)` to stop a previous disposer (if any) and
 * start a new one; or `stop()` and then `start(...)` to obtain a
 * "pause/restart" behavior.
 */
public class DisposingService {
    private static final String TAG = DisposingService.class.getSimpleName();
    private static TileDisposer disposer = null;

    public static void start(SlaveController sc, Config conf) {
        if (disposer != null) {
            Log.d(TAG, "Old disposer found, stopping it and creating a new one...");
            disposer.stop();
        }
        disposer = new TileDisposer(sc, conf);
        disposer.start();
        Log.d(TAG, "New disposer for " + sc.getClass().getSimpleName() + " started.");
    }

    public static void stop() {
        if (disposer == null) {
            Log.d(TAG, "No disposer to stop.");
            return;
        }
        disposer.stop();
        disposer = null;
        Log.d(TAG, "Disposer stopped.");
    }

    private static class TileDisposer {
        private SlaveController sc;
        private int tileDensity;
        private float baseRtt;
        private boolean tutorialMode;

        private Timer tilePoser;

        private Object busListener;

        public TileDisposer(final SlaveController sc, final Config conf) {
            super();
            this.sc = sc;
            this.tileDensity = conf.getTileDensity();
            this.baseRtt = conf.getDefaultRtt();
            this.tutorialMode = conf.isTutorialMode();
            this.tilePoser = new Timer();
            this.busListener = new Object() {
                @Subscribe
                public void onRttUpdate(RTTUpdateEvent e) {
                    reschedule(e.getRtt());
                }
            };
        }

        private TimerTask getSpawnTask() {
            return new TimerTask() {
                @Override
                public void run() {
                    String tname = "Disposer-" + sc.getClass().getSimpleName() + "-" + sc.hashCode();
                    Thread.currentThread().setName(tname);
                    newTile();
                }
            };
        }

        private void reschedule(float rttInSeconds) {
            tilePoser.cancel();
            tilePoser.purge();

            tilePoser = new Timer();

            long rtt = (long) (rttInSeconds * 1000); // from s to ms
            tilePoser.schedule(getSpawnTask(), rtt / tileDensity, rtt / tileDensity);
        }

        private void newTile() {
            if ((new Random()).nextFloat() > InternalConfig.DISPOSER_HOLE_PROB) {
                Tile t = sc.getTile();
                if (tutorialMode) {
                    boolean rw = sc.checkTile(t);
                    TenBus.get().post(EventFactory.newTutorialTile(t, rw));
                } else {
                    TenBus.get().post(EventFactory.newTile(t));
                }
            }
        }

        public void start() {
            reschedule(baseRtt);
            TenBus.get().register(busListener);
        }

        public void stop() {
            tilePoser.cancel();
            tilePoser.purge();
            tilePoser = null;
            TenBus.get().unregister(busListener);
        }
    }
}
