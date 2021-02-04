package com.example.accenturechallenge.data.network.pokemonapi

import com.example.accenturechallenge.data.network.pokemonapi.response.GetPokemonItemResult
import com.example.accenturechallenge.data.network.pokemonapi.response.GetPokemonsResponse
import com.example.accenturechallenge.utils.POKEMON_PAGE_SIZE
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Pokemon API communication setup via Retrofit.
 */
interface PokemonService {

    @GET("pokemon")
    suspend fun fetchPokemons(
        @Query("offset") offset: Int = 0,
        @Query("limit") itemsPerPage: Int = POKEMON_PAGE_SIZE
    ): GetPokemonsResponse

    @GET("pokemon/{id}")
    suspend fun fetchPokemonDetail(
        @Path("id") pokemonId: String,
    ): GetPokemonItemResult

}