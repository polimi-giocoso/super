package it.playfellas.superapp.ui.slave;

import android.os.AsyncTask;

import it.playfellas.superapp.events.EventFactory;
import it.playfellas.superapp.network.TenBus;

/**
 * Created by Stefano Cappa on 04/09/15.
 */
public class PhotoAsyncTask extends AsyncTask<byte[], Void, Void> {

    @Override
    protected Void doInBackground(byte[]... photoArrays) {
        for (byte[] photoArray : photoArrays) {
            // Escape early if cancel() is called
            if (isCancelled()) break;
            TenBus.get().post(EventFactory.sendPhotoByteArray(photoArray));
        }
        return null;
    }
}