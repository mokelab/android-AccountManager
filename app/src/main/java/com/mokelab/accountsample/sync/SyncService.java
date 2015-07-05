package com.mokelab.accountsample.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Service for SyncAdapter
 */
public class SyncService extends Service {

    private static SyncAdapter sAdapter;
    private static final Object sAdapterLock = new Object();

    @Override
    public void onCreate() {
        super.onCreate();

        synchronized (sAdapterLock) {
            if (sAdapter == null) {
                sAdapter = new SyncAdapter(getApplicationContext(), true);
            }
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return sAdapter.getSyncAdapterBinder();
    }
}
