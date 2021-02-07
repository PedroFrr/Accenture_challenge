package com.example.accenturechallenge.data.network.pokemonapi.response


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GetPokemonItemResult(
    @Json(name = "abilities") val abilities: List<Ability>,
    @Json(name = "base_experience") val baseExperience: Int,
    @Json(name = "height") val height: Int,
    @Json(name = "id") val id: Int,
    @Json(name = "name") val name: String,
    @Json(name = "stats") val stats: List<Stat>,
    @Json(name = "types") val types: List<Type>,
    @Json(name = "weight") val weight: Int
)