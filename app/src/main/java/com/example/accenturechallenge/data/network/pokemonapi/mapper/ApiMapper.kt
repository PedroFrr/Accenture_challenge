package com.example.accenturechallenge.data.network.pokemonapi.mapper

import com.example.accenturechallenge.data.database.entities.DbPokemon
import com.example.accenturechallenge.data.database.entities.DbPokemonAbility
import com.example.accenturechallenge.data.database.entities.DbPokemonDetail
import com.example.accenturechallenge.data.database.entities.DbPokemonType
import com.example.accenturechallenge.data.network.pokemonapi.response.Ability
import com.example.accenturechallenge.data.network.pokemonapi.response.GetPokemonItemResult
import com.example.accenturechallenge.data.network.pokemonapi.response.ResourceResult
import com.example.accenturechallenge.data.network.pokemonapi.response.Type


interface ApiMapper {

    fun mapApiPokemonToModel(apiResource: ResourceResult): DbPokemon

    fun mapApiPokemonDetailToModel(apiPokemon: GetPokemonItemResult): DbPokemonDetail

    fun mapApiResourceAbilityToPokemonAbility(apiResource: ResourceResult): DbPokemonAbility

    fun mapApiResourceTypeToPokemonType(apiResource: ResourceResult): DbPokemonType

    fun mapApiAbilityToPokemonAbility(apiAbility: Ability): DbPokemonAbility

    fun mapApiTypeToPokemonType(apiType: Type): DbPokemonType




}