package it.playfellas.superapp.logic;

import android.util.Log;

import com.crashlytics.android.Crashlytics;

import it.playfellas.superapp.logic.db.TileSelector;
import it.playfellas.superapp.logic.master.MasterController;
import it.playfellas.superapp.logic.master.game1.Master1Color;
import it.playfellas.superapp.logic.master.game1.Master1Direction;
import it.playfellas.superapp.logic.master.game1.Master1Shape;
import it.playfellas.superapp.logic.master.game23.Master2Alternate;
import it.playfellas.superapp.logic.master.game23.Master2Decreasing;
import it.playfellas.superapp.logic.master.game23.Master2Growing;
import it.playfellas.superapp.logic.master.game23.Master2Random;
import it.playfellas.superapp.logic.master.game23.Master3Controller;
import it.playfellas.superapp.logic.slave.SlaveController;
import it.playfellas.superapp.logic.slave.game1.Slave1Color;
import it.playfellas.superapp.logic.slave.game1.Slave1ColorAgain;
import it.playfellas.superapp.logic.slave.game1.Slave1Direction;
import it.playfellas.superapp.logic.slave.game1.Slave1Shape;
import it.playfellas.superapp.logic.slave.game23.Slave2Controller;
import it.playfellas.superapp.logic.slave.game23.Slave3Controller;
import it.playfellas.superapp.tiles.TileColor;
import it.playfellas.superapp.tiles.TileDirection;
import it.playfellas.superapp.tiles.TileShape;

/**
 * Created by affo on 18/09/15.
 * ControllerFactory ensures that only one controller
 * (master or slave) exists at a time.
 * <p/>
 * Use `master*` and `slave*` methods to instantiate a new controller
 * (if another controller is found, `destroy` method is called).
 * These methods take care to init controllers (when needed) for you.
 * Use `destroy` to destroy any controller created previously (using `ControllerFactory`).
 * <p/>
 * If you enjoy logging, chain invocations:
 * ```
 * ControllerFactory.verbose().masterColor(...);
 * ControllerFactory.verbose().destroy();
 * ```
 */
public class ControllerFactory {
    private static final String TAG = ControllerFactory.class.getSimpleName();

    private static final ControllerFactory CF = new ControllerFactory();
    private static MasterController master = null;
    private static SlaveController slave = null;

    public static void destroy() {
        if (master != null) {
            master.destroy();
            Log.d(TAG, master.getClass().getSimpleName() + " destroyed");
        }

        if (slave != null) {
            slave.destroy();
            Log.d(TAG, slave.getClass().getSimpleName() + " destroyed");
        }

        master = null;
        slave = null;
    }

    public static ControllerFactory verbose() {
        if (master != null) {
            Log.d(TAG, master.getClass().getSimpleName() + " found");
        }

        if (slave != null) {
            Log.d(TAG, slave.getClass().getSimpleName() + " found");
        }

        if (master == null && slave == null) {
            Log.d(TAG, "No controller found");
        }

        if (master != null && slave != null) {
            RuntimeException e = new RuntimeException("Two controllers found at a time");
            Log.e(TAG, "This should never happen", e);
            Crashlytics.logException(e);
        }

        return CF;
    }

    private static void afterMasterCreate() {
        Log.d(TAG, master.getClass().getSimpleName() + " created");
    }

    private static void afterSlaveCreate() {
        slave.init();
        Log.d(TAG, slave.getClass().getSimpleName() + " created");
    }


    // masters
    public static Master1Color masterColor(Config1 conf) {
        destroy();
        master = new Master1Color(conf);
        afterMasterCreate();
        return (Master1Color) master;
    }

    public static Master1Shape masterShape(Config1 conf) {
        destroy();
        master = new Master1Shape(conf);
        afterMasterCreate();
        return (Master1Shape) master;
    }

    public static Master1Direction masterDirection(Config1 conf) {
        destroy();
        master = new Master1Direction(conf);
        afterMasterCreate();
        return (Master1Direction) master;
    }

    public static Master2Alternate masterAlternate(TileSelector ts, Config2 conf) {
        destroy();
        master = new Master2Alternate(ts, conf);
        afterMasterCreate();
        return (Master2Alternate) master;
    }

    public static Master2Decreasing masterDecreasing(TileSelector ts, Config2 conf) {
        destroy();
        master = new Master2Decreasing(ts, conf);
        afterMasterCreate();
        return (Master2Decreasing) master;
    }

    public static Master2Growing masterGrowing(TileSelector ts, Config2 conf) {
        destroy();
        master = new Master2Growing(ts, conf);
        afterMasterCreate();
        return (Master2Growing) master;
    }

    public static Master2Random masterRandom(TileSelector ts, Config2 conf) {
        destroy();
        master = new Master2Random(ts, conf);
        afterMasterCreate();
        return (Master2Random) master;
    }

    public static Master3Controller master3(TileSelector ts, Config3 conf) {
        destroy();
        master = new Master3Controller(ts, conf);
        afterMasterCreate();
        return (Master3Controller) master;
    }


    // slaves
    public static Slave1Color slaveColor(TileSelector ts, TileColor baseColor) {
        destroy();
        slave = new Slave1Color(ts, baseColor);
        afterSlaveCreate();
        return (Slave1Color) slave;
    }

    public static Slave1ColorAgain slaveColorAgain(TileSelector ts, TileColor baseColor) {
        destroy();
        slave = new Slave1ColorAgain(ts, baseColor);
        afterSlaveCreate();
        return (Slave1ColorAgain) slave;
    }

    public static Slave1Shape slaveShape(TileSelector ts, TileShape baseShape) {
        destroy();
        slave = new Slave1Shape(ts, baseShape);
        afterSlaveCreate();
        return (Slave1Shape) slave;
    }

    public static Slave1Direction slaveDirection(TileSelector ts, TileDirection baseDirection) {
        destroy();
        slave = new Slave1Direction(ts, baseDirection);
        afterSlaveCreate();
        return (Slave1Direction) slave;
    }

    public static Slave2Controller slave2(TileSelector ts) {
        destroy();
        slave = new Slave2Controller(ts);
        afterSlaveCreate();
        return (Slave2Controller) slave;
    }

    public static Slave3Controller slave3(TileSelector ts) {
        destroy();
        slave = new Slave3Controller(ts);
        afterSlaveCreate();
        return (Slave3Controller) slave;
    }
}
