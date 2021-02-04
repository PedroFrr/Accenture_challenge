package com.example.accenturechallenge.data.repository

import androidx.paging.PagingData
import com.example.accenturechallenge.data.database.entities.DbPokemon
import com.example.accenturechallenge.data.database.entities.DbPokemonFavorites
import kotlinx.coroutines.flow.Flow

interface Repository {

    fun fetchPokemons(): Flow<PagingData<DbPokemon>>

    suspend fun fetchFavoritePokemons(): List<DbPokemon>

    suspend fun favoritePokemon(pokemon: DbPokemon)

    suspend fun fetchPokemonDetail(pokemonId: String): DbPokemon
}