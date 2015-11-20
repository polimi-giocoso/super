package it.playfellas.superapp.ui.slave.game1;

import android.graphics.Bitmap;
import android.os.Bundle;

import it.playfellas.superapp.logic.Config1;
import it.playfellas.superapp.logic.db.TileSelector;
import it.playfellas.superapp.tiles.TileColor;
import it.playfellas.superapp.ui.slave.SlavePresenter;

/**
 * Created by Stefano Cappa on 30/07/15.
 */
public class SlaveGame1ColorFragment extends SlaveGame1Fragment {
    public static final String TAG = "SlaveGame1ColorFragment";

    private Slave1Presenter slave1Presenter;
    private static TileColor tc;

    /**
     * Use this factory method to create a new instance of
     * this fragment.
     * You can't put this method in a superclass because you can't create a static abstract method.
     */
    public static SlaveGame1Fragment newInstance(TileSelector ts, Config1 config1, TileColor tileColor, Bitmap photoBitmap) {
        SlaveGame1Fragment.init(ts, config1, photoBitmap);
        SlaveGame1ColorFragment fragment = new SlaveGame1ColorFragment();
        tc = tileColor;
        return fragment;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (slave1Presenter != null) {
            slave1Presenter.pause();
        }
    }

    @Override
    protected SlavePresenter newSlavePresenter() {
        slave1Presenter = new Slave1Presenter(db, this, config);
        slave1Presenter.initControllerColor(tc);
        return slave1Presenter;
    }

    @Override
    protected void setInvertedBackground(boolean isInverted) {
        super.setInvertedBackground(isInverted);
        if (config.getRule() == 1) {
            // border only color
            getConveyorUp().setGreyscale(isInverted);
            getConveyorDown().setGreyscale(isInverted);
        }
    }
}
