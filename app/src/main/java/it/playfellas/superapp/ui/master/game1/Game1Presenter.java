package it.playfellas.superapp.ui.master.game1;

import android.util.Log;

import it.playfellas.superapp.logic.Config1;
import it.playfellas.superapp.logic.ControllerFactory;
import it.playfellas.superapp.logic.master.MasterController;
import it.playfellas.superapp.network.TenBus;
import it.playfellas.superapp.ui.master.GamePresenter;

public class Game1Presenter extends GamePresenter {
    private static final String TAG = Game1Presenter.class.getSimpleName();
    private Game1Fragment fragment;
    private Config1 config1;
    private MasterController master;

    public Game1Presenter(Game1Fragment fragment, Config1 config) {
        super(fragment, config);

        this.fragment = fragment;
        this.config1 = config;

        TenBus.get().register(this);

        //init() creates the master in superclass, based on config1.getRule()
        //ATTENTION: if you call this line after super.getMaster(),
        //you'll get a NullPointerException!!!
        //Obviously, the master is an instance of the correct concrete master.
        super.init();
        //now that i have the master in superclass i can get its and use in this class
        //getMaster returns a generic MasterController, but it created using a concrete master, based on rule
        //for this reason it will work!!!
        this.master = super.getMaster();

        this.fragment.initCentralImage(config.getNoStages());
        this.master.beginStage();
    }

    @Override
    protected MasterController newMasterController() {
        switch (config1.getRule()) {
            default:
            case 0:
            case 1:
                return ControllerFactory.masterColor(config1);
            case 2:
                return ControllerFactory.masterDirection(config1);
            case 3:
                return ControllerFactory.masterShape(config1);
        }
    }

    @Override
    public void destroy() {
        Log.d(TAG, TAG + " destroy called");
        super.destroy();
        TenBus.get().unregister(this);
        Log.d(TAG, TAG + " destroy finished");
    }
}
