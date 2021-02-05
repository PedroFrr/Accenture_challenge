package com.example.accenturechallenge.data.network.pokemonapi.mapper

import com.example.accenturechallenge.data.database.entities.DbPokemon
import com.example.accenturechallenge.data.database.entities.DbPokemonAbility
import com.example.accenturechallenge.data.database.entities.DbPokemonDetail
import com.example.accenturechallenge.data.network.pokemonapi.response.Ability
import com.example.accenturechallenge.data.network.pokemonapi.response.GetPokemonItemResult
import com.example.accenturechallenge.data.network.pokemonapi.response.PokemonResult
import com.example.accenturechallenge.utils.POKEMON_IMAGE_BASE_URL
import javax.inject.Inject

class ApiMapperImpl @Inject constructor() : ApiMapper {

    //regex used to retrieve pokemonId from its URL
    private val regex = "(\\d+)(?!.*\\d)".toRegex()

    override fun mapApiPokemonToModel(apiPokemon: PokemonResult): DbPokemon = with(apiPokemon) {
        val pokemonId = regex.find(url)?.value //retrieves pokemonId

        DbPokemon(
            id = pokemonId ?: "0",
            url = pokemonId.let { "$POKEMON_IMAGE_BASE_URL$pokemonId.png" } ,
            name = name,
        )
    }

    override fun mapApiPokemonDetailToModel(apiPokemon: GetPokemonItemResult): DbPokemonDetail = with(apiPokemon) {

        DbPokemonDetail(
            pokemonDetailId = id.toString(),
            url = "$POKEMON_IMAGE_BASE_URL$id.png",
            name = name,
            weight = weight.toString(),
            height = height.toString(),
            baseExperience = baseExperience.toString(),
        )
    }

    override fun mapApiAbilityToPokemonAbility(apiAbility: Ability): DbPokemonAbility = with(apiAbility) {
        DbPokemonAbility(
            name = ability.name
        )
    }

}