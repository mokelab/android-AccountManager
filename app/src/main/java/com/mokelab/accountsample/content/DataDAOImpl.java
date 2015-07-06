package com.mokelab.accountsample.content;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.text.TextUtils;

/**
 * Implementation
 */
public class DataDAOImpl implements DataDAO {
    private static final String TABLE_NAME = "memo";

    interface Columns extends BaseColumns {
        String USER_NAME = "username";
        String SERVER_ID = "server_id";
        String MEMO = "memo";
    }

    private static final String SQL_CREATE = "create table " + TABLE_NAME + "(" +
            Columns._ID + " integer primary key autoincrement," +
            Columns.USER_NAME + " text," +
            Columns.SERVER_ID + " text," +
            Columns.MEMO + " text)";

    private final SQLiteOpenHelper mHelper;

    public DataDAOImpl(SQLiteOpenHelper helper) {
        mHelper = helper;
    }

    static void createDB(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE);
    }

    @Override
    public long create(String userId, String serverId, String memo) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Columns.USER_NAME, userId);
        values.put(Columns.SERVER_ID, serverId);
        values.put(Columns.MEMO, memo);

        long id = db.insert(TABLE_NAME, null, values);
        db.close();
        return id;
    }

    @Override
    public Cursor query(String userId, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        // add userId selection
        String[] args;
        if (TextUtils.isEmpty(selection)) {
            selection = Columns.USER_NAME + "=?";
            args = new String[] { userId };
        } else {
            selection += "AND " + Columns.USER_NAME + "=?";
            args = new String[selectionArgs.length + 1];
            System.arraycopy(selectionArgs, 0, args, 0, selectionArgs.length);
            args[args.length - 1] = userId;
        }
        SQLiteDatabase db = mHelper.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, projection, selection, args, null, null, sortOrder);
        db.close();
        return cursor;
    }
}
