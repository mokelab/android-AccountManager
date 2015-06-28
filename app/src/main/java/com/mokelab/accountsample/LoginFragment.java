package com.mokelab.accountsample;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Fragment for login page
 */
public class LoginFragment extends Fragment {
    public static final String ACCOUNT_TYPE = "com.mokelab.accountsample";

    @InjectView(R.id.edit_username)
    EditText mUsernameEdit;

    @InjectView(R.id.edit_password)
    EditText mPasswordEdit;

    private AccountManager mAccountManager;

    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();

        Bundle args = new Bundle();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Activity activity = getActivity();
        mAccountManager = AccountManager.get(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_login, container, false);

        ButterKnife.inject(this, root);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        ButterKnife.reset(this);
    }

    @OnClick(R.id.button_login)
    void loginClicked() {
        // do something
        String username = mUsernameEdit.getText().toString();
        String password = mPasswordEdit.getText().toString();

        LoginActivity activity = (LoginActivity) getActivity();
        if (activity == null) { return; }

        Account account = new Account(username, ACCOUNT_TYPE);
        mAccountManager.addAccountExplicitly(account, password, null);

        Intent intent = new Intent();
        intent.putExtra(AccountManager.KEY_ACCOUNT_NAME, username);
        intent.putExtra(AccountManager.KEY_ACCOUNT_TYPE, ACCOUNT_TYPE);
        activity.setAccountAuthenticatorResult(intent.getExtras());
        activity.setResult(Activity.RESULT_OK, intent);
        activity.finish();
    }
}
