package com.example.accenturechallenge.data.network.pokemonapi

import com.example.accenturechallenge.data.network.pokemonapi.response.GetPokemonItemResult
import com.example.accenturechallenge.data.network.pokemonapi.response.GetResourceResponse
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
    ): GetResourceResponse

    @GET("pokemon/{id}")
    suspend fun fetchPokemonDetail(
        @Path("id") pokemonId: String,
    ): GetPokemonItemResult

    /**
     * For the below methods limit is set to 500 by default as I don't want to paginate trough these endpoints
     */
    @GET("ability")
    suspend fun fetchPokemonAbilities(
        @Query("limit") itemsPerPage: Int = 500
    ): GetResourceResponse

    @GET("type")
    suspend fun fetchPokemonTypes(
        @Query("limit") itemsPerPage: Int = 500
    ): GetResourceResponse

}