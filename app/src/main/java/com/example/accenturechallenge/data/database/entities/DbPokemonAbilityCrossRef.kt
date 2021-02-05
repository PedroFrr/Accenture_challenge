package com.example.accenturechallenge.data.database.entities

import androidx.room.Entity

@Entity(primaryKeys = ["pokemonDetailId", "abilityId"])
data class DbPokemonAbilityCrossRef(
    val pokemonDetailId: String,
    val abilityId: String
)
