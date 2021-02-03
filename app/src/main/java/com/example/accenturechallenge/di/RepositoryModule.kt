package com.example.accenturechallenge.di

import com.example.accenturechallenge.data.network.mapper.ApiMapper
import com.example.accenturechallenge.data.network.mapper.ApiMapperImpl
import com.example.accenturechallenge.data.repository.Repository
import com.example.accenturechallenge.data.repository.RepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * Class for repository creation, no need to manually setup the instantiation - easier testing
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    /**
     * Creates an instance of [Repository] based on the [RepositoryImpl]
     */
    @Binds
    abstract fun providesRepository(
        impl: RepositoryImpl
    ) : Repository

    /**
     * Creates the [ApiMapper] which is responsible for converting responses from the network into the data model
     */
    @Binds
    abstract fun providesApiMapper(
        impl: ApiMapperImpl
    ) : ApiMapper
}