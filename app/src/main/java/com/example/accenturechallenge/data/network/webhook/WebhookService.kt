package com.example.accenturechallenge.data.network.webhook

import com.example.accenturechallenge.data.network.webhook.request.FavoritePokemonRequest
import retrofit2.http.Body
import retrofit2.http.POST

interface WebhookService {

    //TODO check if it returns something or not
    @POST("pokemon/favorite")
    suspend fun postFavorite(@Body request: FavoritePokemonRequest)
}