package com.example.accenturechallenge.data.network.mapper

import com.example.accenturechallenge.data.database.entities.DbPokemon
import com.example.accenturechallenge.data.network.response.PokemonResult
import javax.inject.Inject

class ApiMapperImpl @Inject constructor() : ApiMapper {

    override fun mapApiPokemonToModel(apiPokemon: PokemonResult): DbPokemon = with(apiPokemon) {
        DbPokemon(
            url = url,
            name = name,
        )
    }

}