package com.example.accenturechallenge.data.network.webhook

import com.example.accenturechallenge.data.Failure
import com.example.accenturechallenge.data.Success
import com.example.accenturechallenge.data.network.webhook.request.FavoritePokemonRequest
import javax.inject.Inject

class WebhookClient @Inject constructor(
    private val webhookService: WebhookService
) {

    suspend fun postFavorite(favoritePokemonRequest: FavoritePokemonRequest) =
        try {
            val response = webhookService.postFavorite(favoritePokemonRequest)
            Success(response)
        } catch (error: Throwable) {
            Failure(error)
        }
}