package it.playfellas.superapp.ui.slave;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import it.playfellas.superapp.R;
import it.playfellas.superapp.network.TenBus;

/**
 * This fragment has an indeterminate progress bar.
 * This class hasn't any methods to change the fragment.
 */
public class WaitingFragment extends Fragment {
    public static final String TAG = WaitingFragment.class.getSimpleName();

    @Bind(R.id.waitingTextView)
    TextView waitingTextView;
    @Bind(R.id.deviceNameTextView)
    TextView deviceNameTextView;
    @Bind(R.id.waitingProgressBar)
    ProgressBar waitingProgressBar;

    private static String message = null;
    private static boolean showDevName = false;

    /**
     * Method to obtain a new Fragment's instance, eventually with a String message parameter.
     *
     * @param msg String to display a custom textview message in this layout.
     * @return This Fragment instance.
     */
    public static WaitingFragment newInstance(String msg, boolean showDeviceName) {
        message = msg;
        showDevName = showDeviceName;
        return new WaitingFragment();
    }

    public WaitingFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.slave_waiting_fragment, container, false);

        //ButterKnife bind version for fragments
        ButterKnife.bind(this, rootView);

        //if the message received in newInstance is different than null i use this parameter to
        //set the textView. Otherwise i don't set anything, because this fragment will display the original
        //default message added in the slave_waiting_fragment.xmlgment.xml file.
        if (message != null) {
            waitingTextView.setText(message);
        }

        if (showDevName) {
            deviceNameTextView.setText(TenBus.get().myBTName());
            deviceNameTextView.setVisibility(View.VISIBLE);
        }

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        waitingProgressBar.getIndeterminateDrawable().setColorFilter(
                getResources().getColor(R.color.orange), PorterDuff.Mode.MULTIPLY);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
