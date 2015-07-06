package com.mokelab.accountsample.content;

import android.database.Cursor;

/**
 * DAO for DATA
 */
public interface DataDAO {
    long create(String userId, String serverId, String memo);

    Cursor query(String userId, String[] projection, String selection, String[] selectionArgs, String sortOrder);
}
