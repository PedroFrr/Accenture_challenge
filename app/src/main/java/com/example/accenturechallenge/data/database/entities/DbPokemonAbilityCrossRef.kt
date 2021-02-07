package com.example.accenturechallenge.data.database.entities

import androidx.room.Entity

/**
 * Many to many table relation between Pokemons and its abilities
 */
@Entity(primaryKeys = ["pokemonDetailId", "abilityId"])
data class DbPokemonAbilityCrossRef(
    val pokemonDetailId: String,
    val abilityId: String
)
