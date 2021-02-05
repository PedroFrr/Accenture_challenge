package com.example.accenturechallenge.data.network.pokemonapi.mapper

import com.example.accenturechallenge.data.database.entities.DbPokemon
import com.example.accenturechallenge.data.database.entities.DbPokemonAbility
import com.example.accenturechallenge.data.database.entities.DbPokemonDetail
import com.example.accenturechallenge.data.network.pokemonapi.response.Ability
import com.example.accenturechallenge.data.network.pokemonapi.response.GetPokemonItemResult
import com.example.accenturechallenge.data.network.pokemonapi.response.PokemonResult


interface ApiMapper {

    fun mapApiPokemonToModel(apiPokemon: PokemonResult): DbPokemon

    fun mapApiPokemonDetailToModel(apiPokemon: GetPokemonItemResult): DbPokemonDetail

    fun mapApiAbilityToPokemonAbility(apiAbility: Ability): DbPokemonAbility

}