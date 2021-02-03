package com.example.accenturechallenge.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.accenturechallenge.data.database.AppDatabase
import com.example.accenturechallenge.data.database.entities.DbPokemon
import com.example.accenturechallenge.data.database.entities.DbRemoteKeys
import com.example.accenturechallenge.data.network.PokemonService
import com.example.accenturechallenge.data.network.mapper.ApiMapper
import com.example.accenturechallenge.utils.POKEMON_API_STARTING_INDEX
import com.example.accenturechallenge.utils.POKEMON_PAGE_SIZE
import retrofit2.HttpException
import java.io.IOException
import java.io.InvalidObjectException
import kotlin.math.absoluteValue

@ExperimentalPagingApi
class PokemonRemoteMediator(
    private val pokemonService: PokemonService,
    private val database: AppDatabase,
    private val apiMapper: ApiMapper
) : RemoteMediator<Int, DbPokemon>() {

    override suspend fun load(loadType: LoadType, state: PagingState<Int, DbPokemon>): MediatorResult {
        val page = when(loadType){
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: POKEMON_API_STARTING_INDEX
            }
            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyForFirstItem(state)
                if (remoteKeys == null) {
                    throw InvalidObjectException("Remote key and the prevKey should not be null")
                }
                val prevKey = remoteKeys.prevKey
                if (prevKey == null) {
                    return MediatorResult.Success(endOfPaginationReached = false)
                }
                remoteKeys.prevKey
            }
            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                if (remoteKeys?.nextKey == null) {
                    throw InvalidObjectException("Remote key should not be null for $loadType")
                }
                remoteKeys.nextKey
            }
        }


        try {
            val apiResponse = pokemonService.fetchPokemons(offset = page?.let{it}, itemsPerPage =  state.config.pageSize)

            val pokemons = apiResponse.pokemonResults.map {  apiMapper.mapApiPokemonToModel(it)}
            val endOfPaginationReached = apiResponse.next == null
            database.withTransaction {
                // clear all tables in the database
                if (loadType == LoadType.REFRESH) {
                    database.remoteKeysDao().clearRemoteKeys()
                    database.pokemonDao().clearAllPokemons()
                }
                val prevKey = if (page == POKEMON_API_STARTING_INDEX) null else page - POKEMON_PAGE_SIZE
                val nextKey = if (endOfPaginationReached) null else page + POKEMON_PAGE_SIZE
                val keys = pokemons.map {
                    DbRemoteKeys(id = it.id, prevKey = prevKey, nextKey = nextKey)
                }
                database.remoteKeysDao().insertAll(keys)
                database.pokemonDao().insertAll(pokemons)
            }
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (exception: IOException) {
            return MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            return MediatorResult.Error(exception)
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, DbPokemon>): DbRemoteKeys? {
        // Get the last page that was retrieved, that contained items.
        // From that last page, get the last item
        return state.pages.lastOrNull() { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { pokemon ->
                // Get the remote keys of the last item retrieved
                database.remoteKeysDao().remoteKeysPokemonId(pokemon.id)
            }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, DbPokemon>): DbRemoteKeys? {
        // Get the first page that was retrieved, that contained items.
        // From that first page, get the first item
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { pokemon ->
                // Get the remote keys of the first items retrieved
                database.remoteKeysDao().remoteKeysPokemonId(pokemon.id)
            }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, DbPokemon>
    ): DbRemoteKeys? {
        // The paging library is trying to load data after the anchor position
        // Get the item closest to the anchor position
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { pokemonId ->
                database.remoteKeysDao().remoteKeysPokemonId(pokemonId)
            }
        }
    }



}