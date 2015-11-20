package it.playfellas.superapp.ui.master.game3;

import android.util.Log;

import it.playfellas.superapp.logic.Config3;
import it.playfellas.superapp.logic.ControllerFactory;
import it.playfellas.superapp.logic.master.MasterController;
import it.playfellas.superapp.network.TenBus;
import it.playfellas.superapp.ui.master.GamePresenter;

public class Game3Presenter extends GamePresenter {
    private static final String TAG = Game3Presenter.class.getSimpleName();
    private Game3Fragment fragment;
    private Config3 config3;
    private MasterController master;

    public Game3Presenter(Game3Fragment fragment, Config3 config) {
        super(fragment, config);

        this.fragment = fragment;
        this.config3 = config;

        TenBus.get().register(this);

        //init() creates the master in superclass, based on config2.getRule()
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
        return ControllerFactory.master3(Game3Fragment.tileSelector, config3);
    }

    @Override
    public void destroy() {
        Log.d(TAG, TAG + " destroy called");
        super.destroy();
        TenBus.get().unregister(this);
        Log.d(TAG, TAG + " destroy finished");
    }
}
