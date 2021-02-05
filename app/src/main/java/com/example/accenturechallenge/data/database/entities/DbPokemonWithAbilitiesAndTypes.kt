package com.example.accenturechallenge.data.database.entities

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

/**
 * Data class representing nested relationships
 * Pokemon M - M Abilities
 * Pokemon M - M Types
 */
data class DbPokemonWithAbilitiesAndTypes(
    @Embedded val pokemon: DbPokemonDetail,

    @Relation(
        parentColumn = "pokemonDetailId",
        entityColumn = "abilityId",
        associateBy = Junction(DbPokemonAbilityCrossRef::class)
    )
    val abilities: List<DbPokemonAbility>,

    @Relation(
        parentColumn = "pokemonDetailId",
        entityColumn = "typeId",
        associateBy = Junction(DbPokemonTypeCrossRef::class)
    )
    val types: List<DbPokemonType>,

)

