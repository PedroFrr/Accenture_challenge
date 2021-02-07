package com.example.accenturechallenge.data.network.webhook.request


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Data class representing the JSON sent to the Webhook.site
 */
@JsonClass(generateAdapter = true)
data class FavoritePokemonRequest(
    @Json(name = "id") val id: String,
    @Json(name = "name") val name: String,
    @Json(name = "timeStamp") val timestamp: Long,
    @Json(name = "wasFavorited") val wasFavorite: Boolean
)