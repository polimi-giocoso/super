package it.playfellas.superapp.ui.master;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import it.playfellas.superapp.ImmersiveAppCompatActivity;
import it.playfellas.superapp.R;
import it.playfellas.superapp.network.TenBus;
import it.playfellas.superapp.ui.master.bluetooth.BluetoothActivity;

public class MasterActivity extends ImmersiveAppCompatActivity {
    private static final String TAG = MasterActivity.class.getSimpleName();
    private static final String GAME_NUM_INTENTNAME = "game_num";

    @Bind(R.id.game1_button)
    Button game1;
    @Bind(R.id.game2_button)
    Button game2;
    @Bind(R.id.game3_button)
    Button game3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setImmersiveStickyMode(getWindow().getDecorView());
        setContentView(R.layout.master_activity);
        super.setKeepAwake();

        ButterKnife.bind(this);
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

    @Override
    public void onBackPressed() {
        showExitDialog();
        //do nothing
    }

    @OnClick(R.id.game1_button)
    public void onClikGame1(View view) {
        startActivity(getGameActivityIntent(1));
    }

    @OnClick(R.id.game2_button)
    public void onClikGame2(View view) {
        startActivity(getGameActivityIntent(2));
    }

    @OnClick(R.id.game3_button)
    public void onClikGame3(View view) {
        startActivity(getGameActivityIntent(3));
    }

    private Intent getGameActivityIntent(int game) {
        Intent intent = new Intent(this, GameActivity.class);
        Bundle b = new Bundle();
        b.putInt(GAME_NUM_INTENTNAME, game);
        intent.putExtra("masterActivity", b);
        return intent;
    }

    private Intent getBluetoothActivityIntent(){
        return new Intent(this, BluetoothActivity.class);
    }

    private void showExitDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                TenBus.get().detach();
                startActivity(getBluetoothActivityIntent());
            }
        });
        builder.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // Do nothing
            }
        });
        builder.setMessage(getString(R.string.exit_message));
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
