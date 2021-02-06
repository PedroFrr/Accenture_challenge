package com.example.accenturechallenge.data.database.entities

import androidx.room.Embedded
import androidx.room.Relation

data class DbPokemonWithFavorites(
    @Embedded val pokemon: DbPokemon,
    @Relation(
        parentColumn = "id",
        entityColumn = "pokemonId"
    )
    val favorite: DbFavorite
)
