package com.example.accenturechallenge.data.network.pokemonapi.response


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GetPokemonsResponse(
    @Json(name = "count") val count: Int,
    @Json(name = "next") val next: String?,
    @Json(name = "previous") val previous: String?,
    @Json(name = "results") val pokemonResults: List<PokemonResult>
)