package com.example.accenturechallenge.data.repository

import androidx.paging.PagingData
import com.example.accenturechallenge.data.Result
import com.example.accenturechallenge.data.database.entities.DbPokemon
import com.example.accenturechallenge.data.network.pokemonapi.response.GetPokemonItemResult
import com.example.accenturechallenge.data.network.pokemonapi.response.GetResourceResponse
import kotlinx.coroutines.flow.Flow

interface Repository {

    fun fetchPokemons(): Flow<PagingData<DbPokemon>>

    fun fetchFavoritePokemons(): Flow<List<DbPokemon>>

    suspend fun favoritePokemon(pokemon: DbPokemon)

    suspend fun fetchPokemonDetail(pokemonId: String): Flow<Result<GetPokemonItemResult>>

    fun getDbPokemonDetail(pokemonId: String): Flow<DbPokemon>

    suspend fun fetchPokemonAbilities(): Result<GetResourceResponse>

    suspend fun fetchPokemonTypes(): Result<GetResourceResponse>
}