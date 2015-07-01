package com.mokelab.accountsample.api;

import android.os.AsyncTask;

/**
 * Implementation
 */
public class UserAPIImpl implements UserAPI {
    @Override
    public void login(String username, String password, final GetTokenListener callback) {
        // mock implementation
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                callback.onSuccess("token1122");
            }
        }.execute();
    }
}
