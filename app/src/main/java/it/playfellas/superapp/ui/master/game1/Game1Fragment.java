package it.playfellas.superapp.ui.master.game1;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import it.playfellas.superapp.R;
import it.playfellas.superapp.logic.Config1;
import it.playfellas.superapp.ui.master.GameFragment;

public class Game1Fragment extends GameFragment {
    public static final String TAG = Game1Fragment.class.getSimpleName();

    private static Config1 config;

    /**
     * Method to obtain a new Fragment's instance.
     *
     * @param config1 The config object
     * @return This Fragment instance.
     */
    public static Game1Fragment newInstance(Config1 config1) {
        Game1Fragment fragment = new Game1Fragment();
        config = config1;
        return fragment;
    }

    public Game1Fragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.master_game_fragment, container, false);

        ButterKnife.bind(this, rootView);

        //Create the presenter
        super.presenter = new Game1Presenter(this, config);

        super.initPhotos();

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (super.photoimageViews == null) {
            Log.e(TAG, "ImageView or playerBitmaps are null");
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
