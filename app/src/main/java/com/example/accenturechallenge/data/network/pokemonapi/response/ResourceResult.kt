package com.example.accenturechallenge.data.network.pokemonapi.response


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ResourceResult(
    @Json(name = "name") val name: String,
    @Json(name = "url") val url: String
)