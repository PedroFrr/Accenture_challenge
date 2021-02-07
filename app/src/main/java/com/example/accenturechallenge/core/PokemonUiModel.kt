package com.example.accenturechallenge.core

import com.example.accenturechallenge.data.database.entities.DbPokemonWithOrWithoutFavorites

/**
 * Helper Pokemon Model to insert separators between the Pokemons generation
 */
sealed class PokemonUiModel {
    data class PokemonItem(val pokemonResult: DbPokemonWithOrWithoutFavorites) : PokemonUiModel()
    data class SeparatorItem(val description: String) : PokemonUiModel()
}

/*
Defines to which generation the Pokemon belongs to
 */
val PokemonUiModel.PokemonItem.generation: String
    get() = when (pokemonResult.pokemon.id.toInt()) {
        in 1..151 -> "I"
        in 152..251 -> "II"
        in 252..386 -> "III"
        in 387..493 -> "IV"
        in 494..649 -> "V"
        in 650..721 -> "VI"
        in 722..807 -> "VII"
        else -> "Unknown"
    }

