package com.example.accenturechallenge.data.database.entities

import androidx.room.Embedded
import androidx.room.Relation

/**
 * One to one relation between Pokemons and Favorites
 */
data class DbPokemonWithOrWithoutFavorites(
    @Embedded val pokemon: DbPokemon,
    @Relation(
        parentColumn = "id",
        entityColumn = "pokemonId"
    )
    val favorite: DbFavorite?
)
