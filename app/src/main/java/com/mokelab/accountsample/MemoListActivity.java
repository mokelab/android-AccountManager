package com.mokelab.accountsample;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Displays memo list
 */
public class MemoListActivity extends AppCompatActivity {

    private static final String ARGS_USER_ID = "userId";
    private static final int ID_LOADER = 1;

    @InjectView(android.R.id.list)
    ListView mListView;

    @InjectView(R.id.edit)
    EditText mEdit;

    private String mUserId;
    private ContentResolver mContentResolver;
    private CursorAdapter mAdapter;

    public static Intent createIntent(Context context, String userId) {
        Intent it = new Intent(context, MemoListActivity.class);
        it.putExtra(ARGS_USER_ID, userId);
        return it;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        Intent it = getIntent();
        mUserId = it.getStringExtra(ARGS_USER_ID);
        if (mUserId == null) {
            finish();
            return;
        }

        ButterKnife.inject(this, this);

        mContentResolver = getContentResolver();
        mAdapter = new SimpleCursorAdapter(this, R.layout.item_memo, null,
                new String[]{"memo"}, new int[]{android.R.id.text1}, 0);
        mListView.setAdapter(mAdapter);

        loadData();
    }

    private void loadData() {
        LoaderManager manager = getSupportLoaderManager();
        manager.initLoader(ID_LOADER, null, mCallback);
    }

    private LoaderManager.LoaderCallbacks<Cursor> mCallback = new LoaderManager.LoaderCallbacks<Cursor>() {
        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            return new CursorLoader(getApplicationContext(), Uri.parse("content://com.mokelab.memo.provider/users/" + mUserId + "/memos"),
                    null, null, null, null);
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            mAdapter.swapCursor(data);
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {
            mAdapter.swapCursor(null);
        }
    };

    @OnClick(R.id.button_submit)
    void submitClicked() {
        ContentValues values = new ContentValues();
        values.put("memo", mEdit.getText().toString());
        mContentResolver.insert(Uri.parse("content://com.mokelab.memo.provider/users/" + mUserId + "/memos/local"),
                values);

        mEdit.setText("");
    }
}
