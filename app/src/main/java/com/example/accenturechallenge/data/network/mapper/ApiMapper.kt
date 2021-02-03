package com.example.accenturechallenge.data.network.mapper

import com.example.accenturechallenge.data.database.entities.DbPokemon
import com.example.accenturechallenge.data.network.response.PokemonResponse


interface ApiMapper {

    fun mapApiPokemonToModel(apiPokemon: PokemonResponse): DbPokemon

}