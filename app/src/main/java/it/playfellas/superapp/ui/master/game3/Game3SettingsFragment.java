package it.playfellas.superapp.ui.master.game3;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import it.playfellas.superapp.InternalConfig;
import it.playfellas.superapp.R;
import it.playfellas.superapp.logic.Config;
import it.playfellas.superapp.logic.Config3;
import it.playfellas.superapp.network.TenBus;
import it.playfellas.superapp.ui.master.SettingsFragment;
import it.playfellas.superapp.ui.master.StartGameListener;


public class Game3SettingsFragment extends SettingsFragment {

    public static final String TAG = "Game3SettingsFragment";

    private Config3 config;

    /**
     * Method to obtain a new Fragment's instance.
     *
     * @return This Fragment instance.
     */
    public static Game3SettingsFragment newInstance() {
        return new Game3SettingsFragment();
    }

    public Game3SettingsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.master_game3_settings_fragment, container, false);

        ButterKnife.bind(this, rootView);

        super.scorePerStageSeekBar.setEnabled(false);

        return rootView;
    }

    @Override
    protected int getPreferencesId() {
        return R.string.preference_key_game3;
    }

    @Override
    public void onStartGame(StartGameListener l) {
        l.startGame3(config);
    }

    @Override
    protected Config newConfig() {
        config = new Config3();

        //update the config object in the super class with other parameters
        config.setMaxScore(InternalConfig.NO_FIXED_TILES * TenBus.get().noDevices());

        return config;
    }

    @Override
    protected void showPreferences() {
        //Not used here
    }

    @Override
    protected Config setPreferences(SharedPreferences.Editor editor) {
        //update the config object in the super class with other parameters
        config.setMaxScore(InternalConfig.NO_FIXED_TILES * TenBus.get().noDevices());
        return config;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}