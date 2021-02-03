package com.example.accenturechallenge.data.network.mapper

import com.example.accenturechallenge.data.database.entities.DbPokemon
import com.example.accenturechallenge.data.network.response.PokemonResult
import com.example.accenturechallenge.utils.POKEMON_IMAGE_BASE_URL
import javax.inject.Inject

class ApiMapperImpl @Inject constructor() : ApiMapper {

    //regex used to retrieve pokemonId from its URL
    private val regex = "(\\d+)(?!.*\\d)".toRegex()

    override fun mapApiPokemonToModel(apiPokemon: PokemonResult): DbPokemon = with(apiPokemon) {
        val pokemonId = regex.find(url)?.value ?: "" //retrieves pokemonId if not found return empty

        DbPokemon(
            url = "$POKEMON_IMAGE_BASE_URL$pokemonId.png",
            name = name,
        )
    }

}