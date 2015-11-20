package it.playfellas.superapp.ui;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import it.playfellas.superapp.ImmersiveAppCompatActivity;
import it.playfellas.superapp.R;
import it.playfellas.superapp.logic.db.DbAccess;
import it.playfellas.superapp.logic.db.DbException;
import it.playfellas.superapp.logic.db.DbFiller;
import it.playfellas.superapp.network.TenBus;
import it.playfellas.superapp.ui.master.bluetooth.BluetoothActivity;
import it.playfellas.superapp.ui.master.bluetooth.FastStartActivity;
import it.playfellas.superapp.ui.slave.SlaveActivity;

public class MainActivity extends ImmersiveAppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int REQUEST_ENABLE_BT = 2;
    private static final String DBFILL_PREF = "dbfill";

    private BluetoothAdapter mBluetoothAdapter = null;

    @Bind(R.id.logo_splash_screen_imageview)
    ImageView logoSplashScreenImageView;

    @Bind(R.id.masterButton)
    Button masterButton;

    @Bind(R.id.slaveButton)
    Button slaveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setImmersiveStickyMode(getWindow().getDecorView());
        super.setKeepAwake();
        setContentView(R.layout.main_activity);
        ButterKnife.bind(this);

        checkBluetooth();

        this.showSplashScreen();

        // Fill internal DB
        SharedPreferences pref = getSharedPreferences(getString(R.string.preference_key_app), Context.MODE_PRIVATE);
        if (!pref.getBoolean(DBFILL_PREF, false)) {
            // /fill the db.
            //this is A VERY LONG OPERATION and for this reason we are using an AsyncTask
            (new DbFillerAsyncTask(this)).execute();
            pref.edit().putBoolean(DBFILL_PREF, true).apply();
        } else {
            this.activityReady();
        }

        if (FastStartPreferences.isMaster(this)) {
            // I was a master
            startActivity(new Intent(this, FastStartActivity.class));
        }
        // nothing was set before, go on as nothing has happened
    }

    @Override
    protected void onResume() {
        super.onResume();
        // destroy any possible connection created before
        TenBus.get().detach();
    }

    @Override
    public void onStart() {
        super.onStart();
        // If BT is disabled, request that it be enabled.
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_ENABLE_BT) {
            // When the request to enable Bluetooth returns
            if (resultCode == Activity.RESULT_OK) {
                Log.d(TAG, "BlueTooth enabled -> everything is ok!");
            } else {
                // User did not enable Bluetooth or an error occurred
                Log.d(TAG, "BlueTooth not enabled");
                Toast.makeText(this, R.string.bt_not_enabled_leaving, Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    private void checkBluetooth() {
        // If the adapter is null, then Bluetooth is not supported
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth non disponibile", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    private void showSplashScreen() {
        getWindow().getDecorView().setBackground(getResources().getDrawable(R.drawable._splash_screen));
        logoSplashScreenImageView.setVisibility(View.VISIBLE);
        masterButton.setEnabled(false);
        masterButton.setVisibility(View.INVISIBLE);
        slaveButton.setEnabled(false);
        slaveButton.setVisibility(View.INVISIBLE);
    }

    private void activityReady() {
        getWindow().getDecorView().setBackground(getResources().getDrawable(R.drawable._sfondo_grigio_xml));
        logoSplashScreenImageView.setVisibility(View.INVISIBLE);
        masterButton.setEnabled(true);
        masterButton.setVisibility(View.VISIBLE);
        slaveButton.setEnabled(true);
        slaveButton.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.masterButton)
    public void onClickMasterButton(View view) {
        FastStartPreferences.setMaster(this, true);
        startActivity(new Intent(this, BluetoothActivity.class));
    }

    @OnClick(R.id.slaveButton)
    public void onClickSlaveButton(View view) {
        FastStartPreferences.setMaster(this, false);
        startActivity(new Intent(this, SlaveActivity.class));
    }

    public class DbFillerAsyncTask extends AsyncTask<Void, Void, Void> {
        private Context context;

        public DbFillerAsyncTask(Context context) {
            this.context = context;
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                (new DbFiller(new DbAccess(this.context))).fill();
            } catch (DbException e) {
                Log.e("DBFillerAsyncTask", "Filling db error!", e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    activityReady();
                    YoYo.with(Techniques.FadeIn).duration(800).playOn(masterButton);
                    YoYo.with(Techniques.FadeIn).duration(800).playOn(slaveButton);
                }
            });
        }
    }
}
