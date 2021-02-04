package com.example.accenturechallenge.di

import com.example.accenturechallenge.data.network.pokemonapi.PokemonClient
import com.example.accenturechallenge.data.network.pokemonapi.PokemonService
import com.example.accenturechallenge.utils.POKEMON_API_BASE_URL
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
object PokemonApiNetworkModule {

    /**
     *  [Named] used to differentiate between two API which both use Retrofit
     */
    @Provides
    @Singleton
    @Named("retrofit_pokemon_api")
    fun buildPokemonApiRetrofit(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .client(client)
            .baseUrl(POKEMON_API_BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create().asLenient())
            .build()
    }

    @Provides
    fun providePokemonService(@Named("retrofit_pokemon_api") retrofit: Retrofit): PokemonService =
        retrofit.create(PokemonService::class.java)

    @Provides
    fun providePokemonClient(pokemonService: PokemonService): PokemonClient = PokemonClient(pokemonService)


}