package com.example.accenturechallenge.data.repository

import androidx.paging.PagingData
import com.example.accenturechallenge.data.network.response.PokemonResult
import kotlinx.coroutines.flow.Flow

interface Repository {

    fun fetchPokemons(): Flow<PagingData<PokemonResult>>
}