package com.example.accenturechallenge.data.network.pokemonapi.mapper

import com.example.accenturechallenge.data.database.entities.DbPokemon
import com.example.accenturechallenge.data.network.pokemonapi.response.PokemonResult
import com.example.accenturechallenge.utils.POKEMON_IMAGE_BASE_URL
import java.util.*
import javax.inject.Inject

class ApiMapperImpl @Inject constructor() : ApiMapper {

    //regex used to retrieve pokemonId from its URL
    private val regex = "(\\d+)(?!.*\\d)".toRegex()

    override fun mapApiPokemonToModel(apiPokemon: PokemonResult): DbPokemon = with(apiPokemon) {
        val pokemonId = regex.find(url)?.value  //retrieves pokemonId

        DbPokemon(
            id = pokemonId ?: UUID.randomUUID().toString(),
            url = pokemonId.let { "$POKEMON_IMAGE_BASE_URL$pokemonId.png" } ?: "",
            name = name,
        )
    }

}