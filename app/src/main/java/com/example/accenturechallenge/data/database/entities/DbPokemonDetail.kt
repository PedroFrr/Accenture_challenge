package com.example.accenturechallenge.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

//TODO for now I'm separating into another entity. Probably can pout it all on DbPokemon
@Entity(tableName = "pokemon_detail")
data class DbPokemonDetail(
    @PrimaryKey val pokemonDetailId: String = UUID.randomUUID().toString(),
    val url: String,
    val name: String,
    val weight: String,
    val height: String,
    val baseExperience: String,

)
