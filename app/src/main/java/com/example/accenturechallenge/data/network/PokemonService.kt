package com.example.accenturechallenge.data.network

import com.example.accenturechallenge.data.network.response.PokemonResponse
import com.example.accenturechallenge.utils.POKEMON_PAGE_SIZE
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Pokemon API communication setup via Retrofit.
 */
interface PokemonService {

    @GET("pokemon")
    suspend fun fetchPokemons(
        @Query("offset") offset: Int = 0,
        @Query("limit") itemsPerPage: Int = POKEMON_PAGE_SIZE
    ): PokemonResponse
}