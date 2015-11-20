package it.playfellas.superapp.ui.slave;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.widget.Toast;

import com.squareup.otto.Subscribe;

import butterknife.ButterKnife;
import it.playfellas.superapp.ImmersiveAppCompatActivity;
import it.playfellas.superapp.R;
import it.playfellas.superapp.events.bt.BTConnectedEvent;
import it.playfellas.superapp.events.bt.BTDisconnectedEvent;
import it.playfellas.superapp.network.TenBus;
import it.playfellas.superapp.ui.MainActivity;

/**
 * Created by Stefano Cappa on 30/07/15.
 */
public class SlaveActivity extends ImmersiveAppCompatActivity {
    private static final String TAG = SlaveActivity.class.getSimpleName();

    private BluetoothAdapter mBluetoothAdapter = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setImmersiveStickyMode(getWindow().getDecorView());
        setContentView(R.layout.slave_activity);
        super.setKeepAwake();

        ButterKnife.bind(this);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        this.listen();

        //the first thing to do is to start the waiting fragment to wait for a connection
        //when the connection will be available this activity receives BTConnectedEvent and replace the fragment
        //with the photoFragment, as in method "onBTConnectedEvent"
        //i pass null to newInstance of WaitingFragment to specify that i want the default behaviour with the standard
        //message. In recallWaitingFragment(String message) i'll pass a message.
        //WaitingFragment.newInstance:
        // - the first parameter is a custom message to display,
        // - the second one is boolean. If true display the local device name, otherwise not.
        this.changeFragment(WaitingFragment.newInstance(null, true), WaitingFragment.TAG);
    }

    @Override
    protected void onStart() {
        super.onStart();
        TenBus.get().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        TenBus.get().unregister(this);
    }

    private void listen() {
        this.ensureDiscoverable();
        TenBus.get().attach(null);
    }

    private void changeFragment(Fragment fragment, String tag) {
        FragmentTransaction fragmentTransaction = this.getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.slave_root_container, fragment, tag);
        executePendingTransactions(fragmentTransaction);
    }

    private void executePendingTransactions(FragmentTransaction fragmentTransaction) {
        fragmentTransaction.commit();
        this.getSupportFragmentManager().executePendingTransactions();
    }

    /**
     * Makes this device discoverable.
     */
    private void ensureDiscoverable() {
        if (mBluetoothAdapter.getScanMode() != BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
            Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
            startActivity(discoverableIntent);
        }
    }

    @Subscribe
    public void onBTConnectedEvent(BTConnectedEvent event) {
        Toast.makeText(this, event.getDevice().getName() + " connesso!", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, GameActivity.class));
    }

    @Subscribe
    public void onBTDisconnectedEvent(BTDisconnectedEvent event) {
        Toast.makeText(this, event.getDevice().getName() + " disconnesso!", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, MainActivity.class));
    }
}
