package it.playfellas.superapp.ui.master;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;

import butterknife.Bind;
import butterknife.OnClick;
import it.playfellas.superapp.R;
import it.playfellas.superapp.logic.Config;

/**
 * Created by Stefano Cappa on 31/07/15.
 */
public abstract class SettingsFragment extends Fragment {

    private static final String DIFFICULTY_LEVEL = "difficultyLevel";
    private static final String TILE_DENSITY = "tileDensity";
    private static final String SCORE_PER_STAGE = "scorePerStage";
    private static final String NUM_STAGES = "numStages";
    private static final String SPEEDUP = "speedUp";

    @Bind(R.id.difficultyLevelSpinner)
    Spinner difficultyLevelSpinner;
    @Bind(R.id.tileDensitySeekBar)
    DiscreteSeekBar tileDensitySeekBar;
    @Bind(R.id.scorePerStageSeekBar)
    protected DiscreteSeekBar scorePerStageSeekBar;
    @Bind(R.id.numStagesSeekBar)
    DiscreteSeekBar numStagesSeekBar;
    @Bind(R.id.speedUpCheckBox)
    CheckBox speedUpCheckBox;
    @Bind(R.id.tutorialCheckBox)
    CheckBox tutorialCheckBox;

    @Bind(R.id.backButton)
    Button backButton;
    @Bind(R.id.startButton)
    Button startButton;

    protected SharedPreferences.Editor editor;
    protected SharedPreferences sharedPref;

    private StartGameListener mListener;
    private Config config;


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //init sharedPref in superclass
        sharedPref = getActivity().getSharedPreferences(
                getString(getPreferencesId()), Context.MODE_PRIVATE);

        this.initSpinner(difficultyLevelSpinner, R.array.difficulty_string_array);

        //get preferences
        this.readPreferences();
    }

    protected void initSpinner(Spinner spinner, int textArrayResId) {
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                textArrayResId, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
    }

    /**
     * Method to read preferences
     */
    private void readPreferences() {
        config = newConfig();
        config.setDifficultyLevel(sharedPref.getInt(DIFFICULTY_LEVEL, 4));
        config.setTileDensity(sharedPref.getInt(TILE_DENSITY, 4));
        //max score it the score for every stage.
        config.setMaxScore(sharedPref.getInt(SCORE_PER_STAGE, 4));
        // NoStages it the number of stages to complete the entire game.
        config.setNoStages(sharedPref.getInt(NUM_STAGES, 4));
        config.setSpeedUp(sharedPref.getBoolean(SPEEDUP, false));
        this.updateGui(config);
    }

    /**
     * Method to save preferences.
     */
    private void savePreferences() {
        if (config == null) {
            return;
        }
        this.editor = sharedPref.edit();

        config = setPreferences(editor);

        config.setDifficultyLevel(difficultyLevelSpinner.getSelectedItemPosition());
        config.setTileDensity(tileDensitySeekBar.getProgress());
        config.setMaxScore(scorePerStageSeekBar.getProgress());
        config.setNoStages(numStagesSeekBar.getProgress());
        config.setSpeedUp(speedUpCheckBox.isChecked());
        config.setTutorialMode(tutorialCheckBox.isChecked());

        editor.putInt(DIFFICULTY_LEVEL, config.getDifficultyLevel());
        editor.putInt(TILE_DENSITY, config.getTileDensity());
        editor.putInt(SCORE_PER_STAGE, config.getMaxScore());
        editor.putInt(NUM_STAGES, config.getNoStages());
        editor.putBoolean(SPEEDUP, config.isSpeedUp());

        //save all preferences, common, and specific defined here
        editor.apply();
    }

    private void updateGui(Config config) {
        difficultyLevelSpinner.setSelection(config.getDifficultyLevel());
        tileDensitySeekBar.setProgress(config.getTileDensity());
        scorePerStageSeekBar.setProgress(config.getMaxScore());
        numStagesSeekBar.setProgress(config.getNoStages());
        speedUpCheckBox.setChecked(config.isSpeedUp());
        tutorialCheckBox.setChecked(config.isTutorialMode());
        showPreferences();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (StartGameListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement " + StartGameListener.class.getSimpleName());
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @OnClick(R.id.startButton)
    public void onClickStartButton(View view) {
        if (mListener != null) {
            this.savePreferences();
            onStartGame(mListener);
        }
    }

    @OnClick(R.id.backButton)
    public void onClickBackButton(View v) {
        this.startActivity(new Intent(this.getActivity(), MasterActivity.class));
    }

    protected abstract void onStartGame(StartGameListener l);

    protected abstract Config newConfig();

    protected abstract void showPreferences();

    protected abstract Config setPreferences(SharedPreferences.Editor editor);

    protected abstract int getPreferencesId();
}
