package it.playfellas.superapp.ui.master;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import it.playfellas.superapp.InternalConfig;
import it.playfellas.superapp.R;
import it.playfellas.superapp.ui.GameDialogFragment;

/**
 * Created by Stefano Cappa on 05/09/15.
 */
public class MasterTimerDialogFragment extends GameDialogFragment {

    @Bind(R.id.countDownTextView)
    TextView countDownTextView;

    public interface DialogTimerListener {
        void onCountdownFinished();
    }

    /**
     * Method to obtain a new Fragment's instance.
     *
     * @return This Fragment instance.
     */
    public static MasterTimerDialogFragment newInstance() {
        return new MasterTimerDialogFragment();
    }

    /**
     * Default Fragment constructor.
     */
    public MasterTimerDialogFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.master_timer_endstage_dialog, container, false);

        //ButterKnife bind version for fragments
        ButterKnife.bind(this, v);

        countDownTextView.setText(InternalConfig.MASTER_DIAG_COUNTDOWN + "");

        //call this on this fragment, not on the dialog
        setCancelable(false);

        CountDownTimer countDownTimer = new CountDownTimer(InternalConfig.MASTER_DIAG_COUNTDOWN * 1000, 1000) {

            public void onTick(long millisUntilFinished) {
                countDownTextView.setText((millisUntilFinished / 1000) + "");
            }

            public void onFinish() {
                countDownTextView.setText("Avvio in corso...");
                //Go back to che caller fragment.
                ((DialogTimerListener) getTargetFragment()).onCountdownFinished();
                //and dismiss
                dismiss();
            }
        };
        countDownTimer.start();

        return v;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
