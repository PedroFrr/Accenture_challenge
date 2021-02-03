package com.example.accenturechallenge.data.network.mapper

import com.example.accenturechallenge.data.database.entities.DbPokemon
import com.example.accenturechallenge.data.network.response.PokemonResult


interface ApiMapper {

    fun mapApiPokemonToModel(apiPokemon: PokemonResult): DbPokemon

}