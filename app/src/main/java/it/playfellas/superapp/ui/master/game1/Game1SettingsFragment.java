package it.playfellas.superapp.ui.master.game1;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;

import butterknife.Bind;
import butterknife.ButterKnife;
import it.playfellas.superapp.R;
import it.playfellas.superapp.logic.Config;
import it.playfellas.superapp.logic.Config1;
import it.playfellas.superapp.ui.master.SettingsFragment;
import it.playfellas.superapp.ui.master.StartGameListener;


public class Game1SettingsFragment extends SettingsFragment {

    public static final String TAG = "Game1SettingsFragment";

    private static final String RULE = "rule";
    private static final String RULE_CHANGE = "ruleChange";

    @Bind(R.id.ruleSpinner)
    Spinner ruleSpinner;
    @Bind(R.id.ruleChangeSeekBar)
    DiscreteSeekBar invertGameSeekBar;

    private Config1 config;

    /**
     * Method to obtain a new Fragment's instance.
     *
     * @return This Fragment instance.
     */
    public static Game1SettingsFragment newInstance() {
        return new Game1SettingsFragment();
    }

    public Game1SettingsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.master_game1_settings_fragment, container, false);

        ButterKnife.bind(this, rootView);

        super.initSpinner(ruleSpinner, R.array.rulegame1_string_array);

        return rootView;
    }

    @Override
    protected int getPreferencesId() {
        return R.string.preference_key_game1;
    }

    @Override
    public void onStartGame(StartGameListener l) {
        l.startGame1(config);
    }

    @Override
    protected Config newConfig() {
        config = new Config1();

        //update the config object in the super class with other parameters
        config.setRule(super.sharedPref.getInt(RULE, 0));
        config.setRuleChange(super.sharedPref.getInt(RULE_CHANGE, 6));

        return config;
    }

    @Override
    protected void showPreferences() {
        //update specific gui elements for this Fragment using parameter in superclass Config object
        ruleSpinner.setSelection(config.getRule());
        invertGameSeekBar.setProgress(config.getRuleChange());
    }

    @Override
    protected Config setPreferences(SharedPreferences.Editor editor) {
        //update the config object in the super class with other parameters
        config.setRule(ruleSpinner.getSelectedItemPosition());
        config.setRuleChange(invertGameSeekBar.getProgress());

        //update specific settings elements before save
        editor.putInt(RULE, config.getRule());
        editor.putInt(RULE_CHANGE, config.getRuleChange());
        return config;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}