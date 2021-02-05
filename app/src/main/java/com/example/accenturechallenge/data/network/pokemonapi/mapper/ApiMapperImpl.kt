package com.example.accenturechallenge.data.network.pokemonapi.mapper

import com.example.accenturechallenge.data.database.entities.DbPokemon
import com.example.accenturechallenge.data.database.entities.DbPokemonAbility
import com.example.accenturechallenge.data.database.entities.DbPokemonDetail
import com.example.accenturechallenge.data.database.entities.DbPokemonType
import com.example.accenturechallenge.data.network.pokemonapi.response.GetPokemonItemResult
import com.example.accenturechallenge.data.network.pokemonapi.response.ResourceResult
import com.example.accenturechallenge.utils.POKEMON_IMAGE_BASE_URL
import javax.inject.Inject

class ApiMapperImpl @Inject constructor() : ApiMapper {

    //regex used to retrieve pokemonId from its URL
    private val regex = "(\\d+)(?!.*\\d)".toRegex()

    override fun mapApiPokemonToModel(apiResource: ResourceResult): DbPokemon = with(apiResource) {
        val pokemonId = regex.find(url)?.value ?: "0" //retrieves pokemonId, returns 0 if not found

        DbPokemon(
            id = pokemonId,
            url = pokemonId.let { "$POKEMON_IMAGE_BASE_URL$pokemonId.png" },
            name = name,
        )
    }

    override fun mapApiPokemonDetailToModel(apiPokemon: GetPokemonItemResult): DbPokemonDetail =
        with(apiPokemon) {

            DbPokemonDetail(
                pokemonDetailId = id.toString(),
                url = "$POKEMON_IMAGE_BASE_URL$id.png",
                name = name,
                weight = weight.toString(),
                height = height.toString(),
                baseExperience = baseExperience.toString(),
            )
        }

    override fun mapApiAbilityToPokemonAbility(apiResource: ResourceResult): DbPokemonAbility =
        with(apiResource) {
            val abilityId = regex.find(url)?.value ?: "0"
            DbPokemonAbility(
                abilityId = abilityId,
                name = name
            )
        }

    override fun mapApiTypeToPokemonType(apiResource: ResourceResult): DbPokemonType =
        with(apiResource) {
            val typeId = regex.find(url)?.value ?: "0"
            DbPokemonType(
                typeId = typeId,
                name = name
            )
        }

}