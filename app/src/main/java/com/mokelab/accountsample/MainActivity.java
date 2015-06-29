package com.mokelab.accountsample;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CHOOSE_ACCOUNT = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.inject(this, this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) { return; }

        switch (requestCode) {
        case REQUEST_CHOOSE_ACCOUNT: {
            String name = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
            String type = data.getStringExtra(AccountManager.KEY_ACCOUNT_TYPE);

            Log.v("ChooseAccount", "name=" + name + " / type=" + type);
            return;
        }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @OnClick(R.id.button_get_account)
    void getAccountClicked() {
        AccountManager accountManager = AccountManager.get(this);
        Account[] accounts = accountManager.getAccountsByType("com.mokelab.accountsample");
        Log.v("accounts", "length=" + accounts.length);
        for (Account a : accounts) {
            Log.v("accounts", "name=" + a.name);
        }

        // Choose
        Intent it = AccountManager.newChooseAccountIntent(null, null, new String[]{"com.mokelab.accountsample"},
                // true : shows dialog even if number of registered account is 1.
                // false : if number of registered account is 1, API returns it immediately(dialog is not displayed).
                true,
                null, null, null, null);
        startActivityForResult(it, REQUEST_CHOOSE_ACCOUNT);
    }
}
