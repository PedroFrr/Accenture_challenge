package com.example.accenturechallenge.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

/**
 * Database entity which contains the detail for a given Pokemon
 */
@Entity(tableName = "pokemon_detail")
data class DbPokemonDetail(
    @PrimaryKey val pokemonDetailId: String = UUID.randomUUID().toString(),
    val url: String,
    val name: String,
    val weight: String,
    val height: String,
    val baseExperience: String,

)
