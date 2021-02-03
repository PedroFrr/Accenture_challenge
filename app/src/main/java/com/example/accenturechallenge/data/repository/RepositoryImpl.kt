package com.example.accenturechallenge.data.repository

import com.example.accenturechallenge.data.network.PokemonService
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val pokemonService: PokemonService
) : Repository {


}