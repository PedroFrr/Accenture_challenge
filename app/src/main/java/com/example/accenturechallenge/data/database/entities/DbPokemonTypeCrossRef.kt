package com.example.accenturechallenge.data.database.entities

import androidx.room.Entity

/**
 * Many to many table relation between Pokemons and its types
 */
@Entity(primaryKeys = ["pokemonDetailId", "typeId"])
data class DbPokemonTypeCrossRef(
    val pokemonDetailId: String,
    val typeId: String
)
