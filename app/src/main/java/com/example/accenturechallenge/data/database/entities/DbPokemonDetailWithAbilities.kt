package com.example.accenturechallenge.data.database.entities

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class DbPokemonDetailWithAbilities(
    @Embedded val pokemon: DbPokemonDetail,
    @Relation(
        parentColumn = "pokemonDetailId",
        entityColumn = "abilityId",
        associateBy = Junction(DbPokemonAbilityCrossRef::class)
    )
    val abilities: List<DbPokemonAbility>
)
