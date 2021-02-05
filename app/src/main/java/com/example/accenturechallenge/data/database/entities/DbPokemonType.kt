package com.example.accenturechallenge.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "pokemon_type")
data class DbPokemonType(
    @PrimaryKey val typeId: String = UUID.randomUUID().toString(),
    val name: String
)