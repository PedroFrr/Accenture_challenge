package com.example.accenturechallenge.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.accenturechallenge.data.database.entities.DbRemoteKeys

/**
 * When we get the last item loaded from the PagingState we don't know the last ID of the retrieved Pokemon
 * This DAO serves as a workaround to retrieve the previous and next remote keys we need to fetch
 */
@Dao
interface RemoteKeysDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(dbRemoteKey: List<DbRemoteKeys>)

    @Query("SELECT * FROM remote_keys WHERE id = :id")
    suspend fun remoteKeysPokemonId(id: String): DbRemoteKeys?

    @Query("DELETE FROM remote_keys")
    suspend fun clearRemoteKeys()

    @Query("SELECT * FROM remote_keys ORDER BY id DESC")
    suspend fun fetchRemoteKeys(): List<DbRemoteKeys>

}