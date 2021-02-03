package com.example.accenturechallenge.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.accenturechallenge.data.PokemonRemoteMediator
import com.example.accenturechallenge.data.database.AppDatabase
import com.example.accenturechallenge.data.database.entities.DbPokemon
import com.example.accenturechallenge.data.network.PokemonService
import com.example.accenturechallenge.data.network.mapper.ApiMapper
import com.example.accenturechallenge.utils.POKEMON_PAGE_SIZE
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val pokemonService: PokemonService,
    private val database: AppDatabase,
    private val apiMapper: ApiMapper
) : Repository {

    @ExperimentalPagingApi
    override fun fetchPokemons(): Flow<PagingData<DbPokemon>> {
        //TODO delete the below
        /**
         * Paging source if there's a need to rollback
         */
//        return Pager(
//            config = PagingConfig(pageSize = POKEMON_PAGE_SIZE, enablePlaceholders = false),
//            pagingSourceFactory = { PokemonPagingSource(pokemonService) }
//        ).flow

        return Pager(
            config = PagingConfig(
                pageSize = POKEMON_PAGE_SIZE,
                enablePlaceholders = true
            ),
            remoteMediator = PokemonRemoteMediator(
                pokemonService,
                database,
                apiMapper
            ),
            pagingSourceFactory = { database.pokemonDao().fetchAllPokemons()}

        ).flow


    }


}