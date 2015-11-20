package it.playfellas.superapp.ui.slave.game1;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;

import it.playfellas.superapp.logic.Config1;
import it.playfellas.superapp.logic.db.TileSelector;
import it.playfellas.superapp.tiles.TileShape;
import it.playfellas.superapp.ui.slave.SlavePresenter;

/**
 * Created by Stefano Cappa on 30/07/15.
 */
public class SlaveGame1ShapeFragment extends SlaveGame1Fragment {
    public static final String TAG = "SlaveGame1ColorFragment";

    private Slave1Presenter slave1Presenter;
    private static TileShape tShape;

    /**
     * Use this factory method to create a new instance of
     * this fragment.
     * You can't put this method in a superclass because you can't create a static abstract method.
     */
    public static SlaveGame1ShapeFragment newInstance(TileSelector ts, Config1 config1, TileShape tileShape, Bitmap photoBitmap) {
        SlaveGame1Fragment.init(ts, config1, photoBitmap);
        SlaveGame1ShapeFragment fragment = new SlaveGame1ShapeFragment();
        tShape = tileShape;
        return fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (this.slave1Presenter != null) {
            this.slave1Presenter.pause();
        }
    }

    @Override
    protected SlavePresenter newSlavePresenter() {
        slave1Presenter = new Slave1Presenter(db, this, config);
        slave1Presenter.initControllerShape(tShape);
        return slave1Presenter;
    }
}
