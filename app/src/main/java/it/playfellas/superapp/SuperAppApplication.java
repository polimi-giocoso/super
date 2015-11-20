package it.playfellas.superapp;

import android.app.Application;

import com.crashlytics.android.Crashlytics;
import com.firebase.client.Firebase;

import io.fabric.sdk.android.Fabric;

/**
 * Created by affo on 04/09/15.
 */
public class SuperAppApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // Twitter Fabric
        Fabric.with(this, new Crashlytics());

        // Firebase DB
        Firebase.setAndroidContext(this);

        //Butterknife - uncomment if necessary
        //ButterKnife.setDebug(BuildConfig.DEBUG);
    }
}
