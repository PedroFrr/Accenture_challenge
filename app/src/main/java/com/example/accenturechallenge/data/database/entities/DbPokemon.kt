package com.example.accenturechallenge.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "pokemon")
data class DbPokemon(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val url: String,
    val name: String,
)
