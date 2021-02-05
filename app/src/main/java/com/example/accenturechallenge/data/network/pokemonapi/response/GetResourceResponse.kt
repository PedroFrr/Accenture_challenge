package com.example.accenturechallenge.data.network.pokemonapi.response


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Data class representing the response from a response of PokeApi (pokemon, ability, type...)
 */
@JsonClass(generateAdapter = true)
data class GetResourceResponse(
    @Json(name = "count") val count: Int,
    @Json(name = "next") val next: String?,
    @Json(name = "previous") val previous: String?,
    @Json(name = "results") val resourceResults: List<ResourceResult>
)