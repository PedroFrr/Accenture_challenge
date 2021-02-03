package com.example.accenturechallenge.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.accenturechallenge.data.PokemonPagingSource
import com.example.accenturechallenge.data.network.PokemonService
import com.example.accenturechallenge.data.network.response.PokemonResult
import com.example.accenturechallenge.utils.POKEMON_PAGE_SIZE
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val pokemonService: PokemonService
) : Repository {

    override fun fetchPokemons(): Flow<PagingData<PokemonResult>> {
        return Pager(
            PagingConfig(pageSize = POKEMON_PAGE_SIZE, enablePlaceholders = false)
        ) {
            PokemonPagingSource(pokemonService)
        }.flow
    }


}