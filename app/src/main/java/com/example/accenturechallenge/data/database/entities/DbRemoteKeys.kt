package com.example.accenturechallenge.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Data class containing the remote keys used to fetch the next batch of results from the API
 */
@Entity(tableName = "remote_keys")
data class DbRemoteKeys(
    @PrimaryKey val id: String,
    val prevKey: Int?,
    val nextKey: Int?
)
