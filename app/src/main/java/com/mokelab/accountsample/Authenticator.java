package com.mokelab.accountsample;

import android.accounts.AbstractAccountAuthenticator;
import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
import android.accounts.NetworkErrorException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.mokelab.accountsample.api.UserAPI;
import com.mokelab.accountsample.api.UserAPIImpl;

/**
 * Authenticator
 */
public class Authenticator extends AbstractAccountAuthenticator {

    private static final String TOKEN_TYPE = "com.mokelab.accountsample";
    private final Context mContext;
    private final UserAPI mUserAPI;

    public Authenticator(Context context) {
        super(context);
        mContext = context;
        mUserAPI = new UserAPIImpl();
    }

    @Override
    public Bundle editProperties(AccountAuthenticatorResponse accountAuthenticatorResponse, String s) {
        return null;
    }

    @Override
    public Bundle addAccount(AccountAuthenticatorResponse response, String s, String s1, String[] strings, Bundle options) throws NetworkErrorException {
        Intent intent = new Intent(mContext, LoginActivity.class);
        intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response);

        final Bundle bundle = new Bundle();
        bundle.putParcelable(AccountManager.KEY_INTENT, intent);
        return bundle;
    }

    @Override
    public Bundle confirmCredentials(AccountAuthenticatorResponse accountAuthenticatorResponse, Account account, Bundle bundle) throws NetworkErrorException {
        return null;
    }

    @Override
    public Bundle getAuthToken(final AccountAuthenticatorResponse response, final Account account,
                               final String authTokenType, Bundle bundle) throws NetworkErrorException {
        // 1. check authTokenType
        if (!TOKEN_TYPE.equals(authTokenType)) {
            Bundle result = new Bundle();
            result.putString(AccountManager.KEY_ERROR_MESSAGE, "invalid authTokenType");
            return result;
        }

        // 2. get password
        AccountManager accountManager = AccountManager.get(mContext);
        String password = accountManager.getPassword(account);
        if (password == null) {
            Intent it = LoginActivity.createAuthIntent(mContext, response, account, authTokenType);
            Bundle result = new Bundle();
            result.putParcelable(AccountManager.KEY_INTENT, it);
            return bundle;
        }
        // 3. get token
        mUserAPI.login(account.name, password, new UserAPI.GetTokenListener() {
            @Override
            public void onSuccess(String token) {
                Bundle result = new Bundle();
                result.putString(AccountManager.KEY_ACCOUNT_NAME, account.name);
                result.putString(AccountManager.KEY_ACCOUNT_TYPE, "com.mokelab.accountsample");
                result.putString(AccountManager.KEY_AUTHTOKEN, token);
                response.onResult(result);
            }

            @Override
            public void onError(Exception e) {
                response.onError(1, "Failed to get token");
            }
        });
        return null;
    }

    @Override
    public String getAuthTokenLabel(String s) {
        return null;
    }

    @Override
    public Bundle updateCredentials(AccountAuthenticatorResponse accountAuthenticatorResponse, Account account, String s, Bundle bundle) throws NetworkErrorException {
        return null;
    }

    @Override
    public Bundle hasFeatures(AccountAuthenticatorResponse accountAuthenticatorResponse, Account account, String[] strings) throws NetworkErrorException {
        return null;
    }
}
