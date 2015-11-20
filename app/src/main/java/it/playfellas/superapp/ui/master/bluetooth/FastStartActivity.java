package it.playfellas.superapp.ui.master.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.otto.Subscribe;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Semaphore;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import it.playfellas.superapp.ImmersiveAppCompatActivity;
import it.playfellas.superapp.InternalConfig;
import it.playfellas.superapp.R;
import it.playfellas.superapp.events.bt.BTConnectedEvent;
import it.playfellas.superapp.events.bt.BTDisconnectedEvent;
import it.playfellas.superapp.network.TenBus;
import it.playfellas.superapp.ui.FastStartPreferences;
import it.playfellas.superapp.ui.master.MasterActivity;

/**
 * Created by affo on 10/09/15.
 */
public class FastStartActivity extends ImmersiveAppCompatActivity {
    private static final String TAG = FastStartActivity.class.getSimpleName();
    private Map<String, CardViewDevice> players = new HashMap<>();
    private PairTask pairing;

    @Bind(R.id.titleTextView)
    TextView titleTextView;

    @Bind(R.id.cardview1)
    CardView cardview1;
    @Bind(R.id.nameTextView1)
    TextView nameTextView1;
    @Bind(R.id.addressTextView1)
    TextView addressTextView1;
    @Bind(R.id.progressBar1)
    ProgressBar progressBar1;
    @Bind(R.id.countdownTextView1)
    TextView countDownTextView1;

    @Bind(R.id.cardview2)
    CardView cardview2;
    @Bind(R.id.nameTextView2)
    TextView nameTextView2;
    @Bind(R.id.addressTextView2)
    TextView addressTextView2;
    @Bind(R.id.progressBar2)
    ProgressBar progressBar2;
    @Bind(R.id.countdownTextView2)
    TextView countDownTextView2;

    @Bind(R.id.cardview3)
    CardView cardview3;
    @Bind(R.id.nameTextView3)
    TextView nameTextView3;
    @Bind(R.id.addressTextView3)
    TextView addressTextView3;
    @Bind(R.id.progressBar3)
    ProgressBar progressBar3;
    @Bind(R.id.countdownTextView3)
    TextView countDownTextView3;

    @Bind(R.id.cardview4)
    CardView cardview4;
    @Bind(R.id.nameTextView4)
    TextView nameTextView4;
    @Bind(R.id.addressTextView4)
    TextView addressTextView4;
    @Bind(R.id.progressBar4)
    ProgressBar progressBar4;
    @Bind(R.id.countdownTextView4)
    TextView countDownTextView4;

    @Bind(R.id.stopConnectionButton)
    Button exitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setImmersiveStickyMode(getWindow().getDecorView());
        setContentView(R.layout.master_faststart_activity);
        super.setKeepAwake();
        ButterKnife.bind(this);

        String[] addresses = FastStartPreferences.getPlayers(this);

        int nullCount = 0;
        for (String a : addresses) {
            if (a == null) {
                nullCount++;
            }
        }
        if (nullCount == addresses.length) {
            // every address is set to null...
            // enter addresses please...
            startActivity(new Intent(this, BluetoothActivity.class));
            return;
        }

        players.put(addresses[0], new CardViewDevice(cardview1, addressTextView1, nameTextView1, countDownTextView1, progressBar1));
        players.put(addresses[1], new CardViewDevice(cardview2, addressTextView2, nameTextView2, countDownTextView2, progressBar2));
        players.put(addresses[2], new CardViewDevice(cardview3, addressTextView3, nameTextView3, countDownTextView3, progressBar3));
        players.put(addresses[3], new CardViewDevice(cardview4, addressTextView4, nameTextView4, countDownTextView4, progressBar4));

        pairing = new PairTask();
        pairing.execute(addresses);
    }

    @Override
    public void onBackPressed() {
        //do nothing
    }

    private void updateCard(int isCardVisible, String address, String name, int countdown, int working) {
        players.get(address).update(isCardVisible, address, name, countdown, working);
    }

    private void unExit() {
        titleTextView.setText("Connessione automatica in corso");
        exitButton.setEnabled(false);
    }

    private void onEndPairing(boolean error) {
        // a small pause
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Log.d(TAG, "Interrupted while sleeping.");
        }

        if (error) {
            startActivity(new Intent(this, BluetoothActivity.class));
        } else {
            startActivity(new Intent(this, MasterActivity.class));
        }
    }

    @OnClick(R.id.stopConnectionButton)
    public void onStopConnection(View view) {
        pairing.cancel(true);
        onEndPairing(true);
    }

    private class PairTask extends AsyncTask<String, Void, Void> {
        private BluetoothAdapter adapter;
        private boolean error;
        private Semaphore s;

        private void updateCardUI(final int isCardVisible, final String address, final String name,
                                  final int countdown, final int working) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    updateCard(isCardVisible, address, name, countdown, working);
                }
            });
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            this.error = false;
            this.s = new Semaphore(0);
            this.adapter = BluetoothAdapter.getDefaultAdapter();
            TenBus.get().register(this);
        }

        @Override
        protected Void doInBackground(String... players) {
            // a small pause
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                Log.d(TAG, "Interrupted while sleeping.");
            }

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    unExit();
                }
            });

            for (String address : players) {
                if (address != null) {
                    BluetoothDevice device = adapter.getRemoteDevice(address);
                    pair(device, InternalConfig.MAX_BT_CONNECTION_RETRY);
                    updateCardUI(View.VISIBLE, address, device.getName(), 0, View.INVISIBLE);
                }
            }
            return null;
        }

        private void pair(BluetoothDevice device, int limit) {
            if (limit <= 0 || isCancelled()) {
                return;
            }

            String address = device.getAddress();
            String name = device.getName();
            updateCardUI(View.VISIBLE, address, name, limit, View.VISIBLE);

            // a small pause
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                Log.d(TAG, "Interrupted while sleeping.");
            }

            TenBus.get().attach(device);

            // waiting for connection result
            try {
                s.acquire();
            } catch (InterruptedException e) {
                Log.d(TAG, "Interrupted while paring.");
                return;
            }

            if (error) {
                // connection failed...
                // retry.
                pair(device, limit - 1);
            } else {
                updateCardUI(View.VISIBLE, address, name, 0, View.INVISIBLE);
            }
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    onEndPairing(error);
                }
            });
            TenBus.get().unregister(this);
        }

        @Subscribe
        public void onError(BTDisconnectedEvent e) {
            this.error = true;
            s.release();
        }

        @Subscribe
        public void onConnected(BTConnectedEvent e) {
            this.error = false;
            s.release();
        }
    }
}
