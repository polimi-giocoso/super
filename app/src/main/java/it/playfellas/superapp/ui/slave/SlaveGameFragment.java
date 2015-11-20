package it.playfellas.superapp.ui.slave;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.badlogic.gdx.backends.android.AndroidFragmentApplication;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import it.playfellas.superapp.InternalConfig;
import it.playfellas.superapp.R;
import it.playfellas.superapp.Scene;
import it.playfellas.superapp.conveyors.Conveyor;
import it.playfellas.superapp.events.game.EndGameEvent;
import it.playfellas.superapp.events.ui.UIRWEvent;
import it.playfellas.superapp.logic.db.TileSelector;
import it.playfellas.superapp.ui.BitmapUtils;

/**
 * Created by Stefano Cappa on 07/08/15.
 */
public abstract class SlaveGameFragment extends Fragment implements
        AndroidFragmentApplication.Callbacks,
        SceneFragment.FragmentListener {

    @Bind(R.id.photoImageView)
    CircleImageView photoImageView;

    private EndGameListener mListener;
    private Conveyor conveyorUp;
    private Conveyor conveyorDown;
    private SlavePresenter presenter;
    protected static TileSelector db;
    protected static Bitmap photo;
    protected SceneFragment sceneFragment;

    private SoundPool soundPool;
    private int rightSound;
    private int wrongSound;

    public interface EndGameListener {
        void showTrophy();
    }

    public void onRightOrWrong(UIRWEvent e) {
        if (e.isRight()) {
            if (InternalConfig.GUI_DEBUG) {
                Toast.makeText(this.getActivity(), "Right", Toast.LENGTH_SHORT).show();
            }
            soundPool.play(rightSound, 1, 1, 1, 0, 1);
        } else {
            if (InternalConfig.GUI_DEBUG) {
                Toast.makeText(this.getActivity(), "Wrong", Toast.LENGTH_SHORT).show();
            }
            soundPool.play(wrongSound, 1, 1, 1, 0, 1);
        }
    }

    protected static void init(Bitmap photoBitmap) {
        if (photoBitmap != null) {
            photo = BitmapUtils.scaleBitmap(photoBitmap, BitmapUtils.dpToPx(100), BitmapUtils.dpToPx(100));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(getLayoutId(), container, false);

        ButterKnife.bind(this, root);

        sceneFragment = SceneFragment.newInstance();
        getChildFragmentManager().beginTransaction().replace(R.id.scene, sceneFragment).commit();

        conveyorUp = newConveyorUp();
        conveyorDown = newConveyorDown();
        presenter = newSlavePresenter();

        if (photo != null && photoImageView != null) {
            photoImageView.setImageBitmap(photo);
        }

        onCreateView(root);

        initSounds();

        return root;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (EndGameListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement " + EndGameListener.class.getSimpleName());
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void exit() {
        //required by Libgdx, never remove this method!!!
    }

    @Override
    public void onSceneReady(Scene scene) {
        scene.addConveyorUp(conveyorUp);
        scene.addConveyorDown(conveyorDown);
    }

    protected abstract Conveyor newConveyorUp();

    protected abstract Conveyor newConveyorDown();

    protected abstract SlavePresenter newSlavePresenter();

    protected abstract int getLayoutId();

    protected abstract void onCreateView(View root);

    private void initSounds() {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            SoundPool.Builder soundPoolBuilder = new SoundPool.Builder();
            soundPool = soundPoolBuilder.setAudioAttributes(new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build()).build();
        } else {
            //noinspection deprecation
            soundPool = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
        }
        rightSound = soundPool.load(getActivity(), R.raw.right, 1);
        wrongSound = soundPool.load(getActivity(), R.raw.wrong, 1);
    }

    public void endGame(EndGameEvent e) {
        if (e.haveIWon() && mListener != null) {
            mListener.showTrophy();
        } else {
            startActivity(new Intent(this.getContext(), GameActivity.class));
        }
    }
}
