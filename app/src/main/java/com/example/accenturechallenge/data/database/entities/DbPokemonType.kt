package com.example.accenturechallenge.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

/**
 * RefData entity which contains the types a Pokemon can have
 * This table is pre-populated on Database creation
 */
@Entity(tableName = "pokemon_type")
data class DbPokemonType(
    @PrimaryKey val typeId: String = UUID.randomUUID().toString(),
    val name: String
)