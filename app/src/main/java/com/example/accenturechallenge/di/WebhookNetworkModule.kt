package com.example.accenturechallenge.di

import com.example.accenturechallenge.data.network.webhook.WebhookService
import com.example.accenturechallenge.utils.WEBHOOK_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Named
import javax.inject.Singleton

/**
 * Class that provides Retrofit services for remote integration.
 */
@Module
@InstallIn(SingletonComponent::class)
object WebhookNetworkModule {

    @Provides
    @Singleton
    @Named("retrofit_webhook")
    fun buildWebhookRetrofit(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .client(client)
            .baseUrl(WEBHOOK_URL)
            .addConverterFactory(MoshiConverterFactory.create().asLenient())
            .build()
    }

    @Provides
    fun provideWebhookService(@Named("retrofit_webhook") retrofit: Retrofit): WebhookService =
        retrofit.create(WebhookService::class.java)

}