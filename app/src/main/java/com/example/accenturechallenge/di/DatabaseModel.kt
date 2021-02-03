package com.example.accenturechallenge.di

import android.content.Context
import com.example.accenturechallenge.data.database.AppDatabase
import com.example.accenturechallenge.data.database.dao.PokemonDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModel {

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase = AppDatabase.getInstance(context)

    @Provides
    fun providePokemonDao(appDatabase: AppDatabase): PokemonDao = appDatabase.pokemonDao()

}