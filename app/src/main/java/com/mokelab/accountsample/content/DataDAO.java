package com.mokelab.accountsample.content;

/**
 * DAO for DATA
 */
public interface DataDAO {
    long create(String userId, String serverId, String memo);
}
