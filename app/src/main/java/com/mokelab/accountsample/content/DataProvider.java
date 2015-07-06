package com.mokelab.accountsample.content;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

/**
 * ContentProvider
 */
public class DataProvider extends ContentProvider {
    private static final UriMatcher sUriMatcher;

    private static final String AUTHORITY = "com.mokelab.sync.provider";

    private static final int TYPE_MEMO_LIST = 1;
    private static final int TYPE_MEMO_BY_SERVER = 2;
    private static final int TYPE_CREATE_MEMO_BY_LOCAL = 3;
    private static final int TYPE_MEMO_BY_LOCAL = 4;

    public static final String FIELD_MEMO = "memo";
    private static final String MIMETYPE_MEMO_LIST = "vnd.android.cursor.dir/vnd.mokelab.memo";
    private static final String MIMETYPE_MEMO = "vnd.android.cursor.item/vnd.mokelab.memo";

    static {
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sUriMatcher.addURI(AUTHORITY, "users/*/memos", TYPE_MEMO_LIST);
        sUriMatcher.addURI(AUTHORITY, "users/*/memos/server/*", TYPE_MEMO_BY_SERVER);
        sUriMatcher.addURI(AUTHORITY, "users/*/memos/local", TYPE_CREATE_MEMO_BY_LOCAL);
        sUriMatcher.addURI(AUTHORITY, "users/*/memos/local/#", TYPE_MEMO_BY_LOCAL);
    }

    private DataDB mDB;
    private DataDAO mDAO;

    @Override
    public boolean onCreate() {
        mDB = new DataDB(getContext());
        mDAO = new DataDAOImpl(mDB);
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor c = null;
        switch (sUriMatcher.match(uri)) {
        case TYPE_MEMO_LIST:
            c = queryMemoList(uri, projection, selection, selectionArgs, sortOrder);
        }
        return c;
    }

    @Override
    public String getType(Uri uri) {
        switch (sUriMatcher.match(uri)) {
        case TYPE_MEMO_LIST: return MIMETYPE_MEMO_LIST;
        case TYPE_MEMO_BY_LOCAL:
        case TYPE_MEMO_BY_SERVER:
            return MIMETYPE_MEMO;
        }
        return "unknown";
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        Uri result = null;
        switch (sUriMatcher.match(uri)) {
        case TYPE_CREATE_MEMO_BY_LOCAL:
            result = insertLocal(uri, contentValues);
            break;
        }
        return result;
    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        return 0;
    }

    // region private

    private Uri insertLocal(Uri uri, ContentValues contentValues) {
        String userId = uri.getPathSegments().get(1); // 0: "users", 1:userId, 2:"memos"
        String memo = contentValues.getAsString(FIELD_MEMO);
        long id = mDAO.create(userId, "", memo);

        return Uri.parse("content://" + AUTHORITY + "/users/" + userId + "/memos/local/" + id);
    }

    private Cursor queryMemoList(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        String userId = uri.getPathSegments().get(1); // 0: "users", 1:userId, 2:"memos"

        return mDAO.query(userId, projection, selection, selectionArgs, sortOrder);
    }

    // endregion
}
