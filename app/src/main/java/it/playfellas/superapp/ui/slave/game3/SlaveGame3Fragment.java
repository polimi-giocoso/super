package it.playfellas.superapp.ui.slave.game3;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import butterknife.ButterKnife;
import it.playfellas.superapp.R;
import it.playfellas.superapp.conveyors.Conveyor;
import it.playfellas.superapp.conveyors.MovingConveyor;
import it.playfellas.superapp.conveyors.TowerConveyor;
import it.playfellas.superapp.logic.Config3;
import it.playfellas.superapp.logic.db.TileSelector;
import it.playfellas.superapp.tiles.Tile;
import it.playfellas.superapp.ui.slave.MovingConveyorListener;
import it.playfellas.superapp.ui.slave.SlaveGameFragment;
import it.playfellas.superapp.ui.slave.SlavePresenter;
import it.playfellas.superapp.ui.slave.TowerConveyorListener;
import lombok.Getter;

/**
 * Created by Stefano Cappa on 30/07/15.
 */
public class SlaveGame3Fragment extends SlaveGameFragment {
    public static final String TAG = SlaveGame3Fragment.class.getSimpleName();

    @Getter
    private TowerConveyor conveyorUp;
    @Getter
    private MovingConveyor conveyorDown;

    protected static Config3 config;
    private Slave3Presenter slave3Presenter;

    /**
     * Use this factory method to create a new instance of
     * this fragment.
     * You can't put this method in a superclass because you can't create a static abstract method.
     */
    public static SlaveGame3Fragment newInstance(TileSelector ts, Config3 config3, Bitmap photoBitmap) {
        SlaveGame3Fragment fragment = new SlaveGame3Fragment();
        config = config3;
        db = ts;
        init(photoBitmap);
        return fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (this.slave3Presenter != null) {
            this.slave3Presenter.pause();
        }
    }

    @Override
    public void onDestroyView() {
        //TODO why this things are here???? move down after the super.onDestroyView();
        if (conveyorUp != null) {
            conveyorUp.clear();
            conveyorUp.stop();
        }
        if (conveyorDown != null) {
            conveyorDown.clear();
            conveyorDown.stop();
        }
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    protected Conveyor newConveyorUp() {
        conveyorUp = new TowerConveyor(new TowerConveyorListener());
        return conveyorUp;
    }

    @Override
    protected Conveyor newConveyorDown() {
        conveyorDown = new MovingConveyor(new MovingConveyorListener(), 5, MovingConveyor.RIGHT);
        return conveyorDown;
    }

    @Override
    protected SlavePresenter newSlavePresenter() {
        this.slave3Presenter = new Slave3Presenter(db, this, config);
        return this.slave3Presenter;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.slave_game3_fragment;
    }

    @Override
    protected void onCreateView(View root) {
        //this is not the method defined in Fragment, but in SlaveGameFragment as abstract method
    }

    public void updateSlotsStack(Tile[] stack) {
        conveyorUp.updateSlotStack(stack);
    }

    public void updateCompleteStack(Tile[] stack) {
        conveyorUp.updateCompleteStack(stack);
    }
}

