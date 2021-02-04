package com.example.accenturechallenge.di

import com.example.accenturechallenge.data.network.pokemonapi.PokemonService
import com.example.accenturechallenge.data.network.webhook.WebhookService
import com.example.accenturechallenge.utils.POKEMON_API_BASE_URL
import com.example.accenturechallenge.utils.WEBHOOK_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Named
import javax.inject.Singleton

/**
 * Class that provides Retrofit services for remote integration.
 */
@Module
@InstallIn(SingletonComponent::class)
object NetworkingModule {

    @Provides
    @Singleton
    fun provideClient(): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()

}