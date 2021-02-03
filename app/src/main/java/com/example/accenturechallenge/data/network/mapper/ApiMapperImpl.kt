package com.example.accenturechallenge.data.network.mapper

import com.example.accenturechallenge.data.database.entities.DbPokemon
import com.example.accenturechallenge.data.network.response.PokemonResponse
import javax.inject.Inject

class ApiMapperImpl @Inject constructor() : ApiMapper {

    override fun mapApiPokemonToModel(apiPokemon: PokemonResponse): DbPokemon = with(apiPokemon) {
        DbPokemon(
            //TODO change
        )
    }

}