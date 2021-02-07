package com.example.accenturechallenge.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Database table which contains the favorited Pokemons by the user
 */
@Entity(tableName = "favorite_pokemon")
data class DbFavorite(
    @PrimaryKey val pokemonId: String,
)
