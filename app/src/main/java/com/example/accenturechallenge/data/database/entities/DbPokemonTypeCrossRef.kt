package com.example.accenturechallenge.data.database.entities

import androidx.room.Entity

@Entity(primaryKeys = ["pokemonDetailId", "typeId"])
data class DbPokemonTypeCrossRef(
    val pokemonDetailId: String,
    val typeId: String
)
