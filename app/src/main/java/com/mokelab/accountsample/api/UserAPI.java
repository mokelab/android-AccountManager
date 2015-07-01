package com.mokelab.accountsample.api;

/**
 * API for user
 */
public interface UserAPI {
    interface GetTokenListener {
        void onSuccess(String token);
        void onError(Exception e);
    }

    void login(String username, String password, GetTokenListener callback);
}
