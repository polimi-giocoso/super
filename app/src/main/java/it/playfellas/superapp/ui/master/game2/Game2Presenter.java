package it.playfellas.superapp.ui.master.game2;

import android.util.Log;

import it.playfellas.superapp.logic.Config2;
import it.playfellas.superapp.logic.ControllerFactory;
import it.playfellas.superapp.logic.master.MasterController;
import it.playfellas.superapp.network.TenBus;
import it.playfellas.superapp.ui.master.GamePresenter;

public class Game2Presenter extends GamePresenter {
    private static final String TAG = Game2Presenter.class.getSimpleName();
    private Game2Fragment fragment;
    private Config2 config2;
    private MasterController master;

    public Game2Presenter(Game2Fragment fragment, Config2 config) {
        super(fragment, config);

        this.fragment = fragment;
        this.config2 = config;

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
        switch (config2.getGameMode()) {
            default:
            case 0:
                return ControllerFactory.masterGrowing(Game2Fragment.tileSelector, config2);
            case 1:
                return ControllerFactory.masterDecreasing(Game2Fragment.tileSelector, config2);
            case 2:
                return ControllerFactory.masterAlternate(Game2Fragment.tileSelector, config2);
            case 3:
                return ControllerFactory.masterRandom(Game2Fragment.tileSelector, config2);
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
