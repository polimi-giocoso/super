package it.playfellas.superapp.ui.master;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import it.playfellas.superapp.InternalConfig;
import it.playfellas.superapp.R;
import it.playfellas.superapp.events.ui.UIEndGameEvent;
import it.playfellas.superapp.ui.BitmapUtils;


public class GameFragment extends Fragment implements
        MasterTimerDialogFragment.DialogTimerListener {

    private static final String TAG = GameFragment.class.getSimpleName();

    protected GamePresenter presenter;
    protected List<ImageView> photoimageViews = new ArrayList<>();
    protected List<Bitmap> photoBitmaps = new ArrayList<>();

    @Bind(R.id.showMasterInfos)
    Button showMasterInfosButton;
    @Bind(R.id.exitGameButton)
    Button exitGameButton;
    @Bind(R.id.masterInfosRelativeLayout)
    RelativeLayout masterInfosRelativeLayout;
    @Bind(R.id.currentScoreOverTotalTextView)
    TextView currentScoreOverTotalTextView;
    @Bind(R.id.currentStageOverTotalTextView)
    TextView currentStageOverTotalTextView;
    @Bind(R.id.gameIdInDb)
    TextView gameIdInDb;

    //Photos taken on slave devices.
    @Bind(R.id.photo1ImageView)
    protected CircleImageView photo1ImageView;
    @Bind(R.id.photo2ImageView)
    protected CircleImageView photo2ImageView;
    @Bind(R.id.photo3ImageView)
    protected CircleImageView photo3ImageView;
    @Bind(R.id.photo4ImageView)
    protected CircleImageView photo4ImageView;

    //The central image, that represent the progress (in number of completed stages) of the current game.
    @Bind(R.id.central_img)
    ImageView centralImageView;

    private List<Bitmap> piecesList;
    private MediaPlayer mediaPlayer;
    private SoundPool soundPool;
    private int toggleModeSound;
    private int victorySound;
    private boolean firstMusic = true;

    @Override
    public void onCountdownFinished() {
        presenter.beginNextStage();
    }

    /**
     * Method to init central image, creating a grayscale version of {@code centralImageBitmap}.
     *
     * @param numStages the maximum number of stages used to split the original bitmap.
     */
    public void initCentralImage(int numStages) {
        //split the original bitmap and store its pieces in a List
        piecesList = BitmapUtils.splitImage(BitmapFactory.decodeResource(getResources(), R.drawable._master_central_img), numStages);
    }

    /**
     * Method to update the central image coloring {@code currentStages} pieces,
     * and leaving {@code numStages-currentStages} pieces in gray scale.
     *
     * @param currentStage starts from 0 to numStages-1
     * @param numStages    the maximum number of stages
     */
    protected void updateStageImage(int currentStage, int numStages) {
        if (currentStage > numStages) {
            return;
        }

        Log.d(TAG, "updateStageImage: currentStage: " + currentStage + " , maxStages: " + numStages);

        //get the combined image
        Bitmap finalBitmap = BitmapUtils.getNewCombinedByPiecesAlsoGrayscaled(piecesList, currentStage, numStages);

        //set the combined image in the gui
        centralImageView.setImageBitmap(finalBitmap);
    }

    /**
     * Method to update the current stage's score. This is not the global score.
     *
     * @param currentStageScore The total score.
     */
    protected void setCurrentScoreOverTotal(int currentStageScore, int maxScore) {
        this.currentScoreOverTotalTextView.setText(
                "Punteggio: " + currentStageScore + " / " + maxScore);
    }

    protected void setCurrentStageOverTotal(int currentStageNumber, int maxNumStages) {
        this.currentStageOverTotalTextView.setText("Manche: " + currentStageNumber + " / " + maxNumStages);
    }

    protected void setMasterGameId(String gameId) {
        this.gameIdInDb.setText("ID partita: " + gameId);
    }


    public void showDialogToProceed() {
        //show a dialog with this title and string, but this isn't a dialog to ask confirmation
        showMasterTimerDialogFragment("Stage completato", "Pronto per lo stage successivo?",
                InternalConfig.MASTER_DIAG_TAG, InternalConfig.MASTER_DIAG_ID);
    }

    private void showMasterTimerDialogFragment(String title, String message, String tag, int id) {
        MasterTimerDialogFragment masterTimerDialogFragment = getMasterTimerDiagFragment(tag);
        if (masterTimerDialogFragment == null) {
            masterTimerDialogFragment = MasterTimerDialogFragment.newInstance();
            masterTimerDialogFragment.setTargetFragment(this, id);
            masterTimerDialogFragment.show(getFragmentManager(), tag);
            getFragmentManager().executePendingTransactions();
        }
    }

    public void hideMasterTimerDialog() {
        MasterTimerDialogFragment endStageDialogFragment = getMasterTimerDiagFragment(InternalConfig.MASTER_DIAG_TAG);
        if (endStageDialogFragment != null) {
            endStageDialogFragment.dismiss();
        }
    }

    private MasterTimerDialogFragment getMasterTimerDiagFragment(String tag) {
        return (MasterTimerDialogFragment) getFragmentManager().findFragmentByTag(tag);
    }

    protected void initPhotos() {
        this.photoimageViews.add(this.photo1ImageView);
        this.photoimageViews.add(this.photo2ImageView);
        this.photoimageViews.add(this.photo3ImageView);
        this.photoimageViews.add(this.photo4ImageView);
    }

    protected void updateImageViews() {
        for (int i = 0; i < photoBitmaps.size(); i++) {
            if (this.photoimageViews.get(i) == null || photoBitmaps.get(i) == null) {
                Log.e(TAG, "ImageView.get(i) or playerPhoto.get(i) are null");
                continue;
            }
            this.photoimageViews.get(i).setImageBitmap(photoBitmaps.get(i));
        }
    }

    public void updatePhotos(byte[] photoByteArray) {
        photoBitmaps.add(BitmapUtils.scaleBitmap(BitmapUtils.fromByteArraytoBitmap(photoByteArray),
                BitmapUtils.dpToPx(100), BitmapUtils.dpToPx(100)));
        this.updateImageViews();
    }

    public void endGame(UIEndGameEvent e) {
        //return to the Master Activity to choose another game
        this.hideMasterTimerDialog();
        presenter.destroy();
        startActivity(new Intent(this.getContext(), MasterActivity.class));
        recycleMasterCentralImage();
        if (e.won()) {
            soundPool.play(victorySound, 1, 1, 1, 0, 1);
        }
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
    }

    public void recycleMasterCentralImage() {
        for (Bitmap b : photoBitmaps) {
            if (b != null) {
                b.recycle();
            }
        }
    }

    @OnClick(R.id.showMasterInfos)
    public void onMasterInfosButtonClicked(View v) {
        if (masterInfosRelativeLayout.getVisibility() == View.GONE) {
            masterInfosRelativeLayout.setVisibility(View.VISIBLE);
        } else {
            masterInfosRelativeLayout.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.exitGameButton)
    public void onExitGameClicked(View v) {
        presenter.getMaster().endGame();
        //TODO check if it's necessary to call presenter.destroy(); or not
        //presenter.destroy();
    }

    public void initSounds() {
        mediaPlayer = MediaPlayer.create(getActivity().getApplicationContext(), R.raw.bgsound1_loop);
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
        toggleModeSound = soundPool.load(getActivity(), R.raw.toggle_fx, 1);
        victorySound = soundPool.load(getActivity(), R.raw.victory, 1);
    }

    public void playMusic() {
        mediaPlayer.setLooping(true);
        mediaPlayer.start();
    }

    public void stopMusic() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
    }

    public void resetMusic() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            firstMusic = true;
            mediaPlayer = MediaPlayer.create(getActivity().getApplicationContext(), R.raw.bgsound1_loop);
        }
    }

    public void pauseMusic() {
        if (mediaPlayer != null) {
            mediaPlayer.pause();
        }
    }

    public void toggleMusic() {
        firstMusic = !firstMusic;
        stopMusic();
        soundPool.play(toggleModeSound, 1, 1, 1, 0, 1);
        int track = firstMusic ? R.raw.bgsound1_loop : R.raw.bgsound2_loop;
        mediaPlayer.release();
        mediaPlayer = MediaPlayer.create(getActivity().getApplicationContext(), track);
        playMusic();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        soundPool.release();
    }
}
