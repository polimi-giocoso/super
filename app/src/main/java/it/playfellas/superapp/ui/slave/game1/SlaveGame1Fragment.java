package it.playfellas.superapp.ui.slave.game1;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.RelativeLayout;

import butterknife.Bind;
import butterknife.ButterKnife;
import it.playfellas.superapp.R;
import it.playfellas.superapp.conveyors.MovingConveyor;
import it.playfellas.superapp.logic.Config1;
import it.playfellas.superapp.logic.db.TileSelector;
import it.playfellas.superapp.ui.slave.MovingConveyorListener;
import it.playfellas.superapp.ui.slave.SlaveGameFragment;
import lombok.Getter;

/**
 * Created by Stefano Cappa on 30/07/15.
 */
public abstract class SlaveGame1Fragment extends SlaveGameFragment {
    public static final String TAG = SlaveGame1Fragment.class.getSimpleName();

    @Bind(R.id.gameFragmentRelativeLayout)
    RelativeLayout gameFragmentRelativeLayout;

    protected static Config1 config;
    @Getter
    private MovingConveyor conveyorUp;
    @Getter
    private MovingConveyor conveyorDown;

    /**
     * Init method
     */
    public static void init(TileSelector ts, Config1 config1, Bitmap photoBitmap) {
        init(photoBitmap);
        db = ts;
        config = config1;
    }

    @Override
    protected void onCreateView(View root) {
        ButterKnife.bind(this, root);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.slave_game1_fragment;
    }

    @Override
    public void onDestroyView() {
        //this is not the method defined in Fragment, but in SlaveGameFragment as abstract method

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
    protected MovingConveyor newConveyorUp() {
        conveyorUp = new MovingConveyor(new MovingConveyorListener(), 5, MovingConveyor.LEFT);
        return conveyorUp;
    }

    @Override
    protected MovingConveyor newConveyorDown() {
        conveyorDown = new MovingConveyor(new MovingConveyorListener(), 5, MovingConveyor.RIGHT);
        return conveyorDown;
    }


    protected void setInvertedBackground(boolean isInverted) {
        if (isInverted) {
            gameFragmentRelativeLayout.setBackground(getActivity().getResources().getDrawable(R.drawable._sfondo_verde));
        } else {
            gameFragmentRelativeLayout.setBackground(getActivity().getResources().getDrawable(R.drawable._sfondo_arancio));
        }
        sceneFragment.getScene().setInvertedBackground(isInverted);
    }
}
