package com.example.accenturechallenge.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.accenturechallenge.data.database.AppDatabase
import com.example.accenturechallenge.data.database.entities.DbPokemonWithOrWithoutFavorites
import com.example.accenturechallenge.data.database.entities.DbRemoteKeys
import com.example.accenturechallenge.data.network.pokemonapi.PokemonClient
import com.example.accenturechallenge.data.network.pokemonapi.mapper.ApiMapper
import com.example.accenturechallenge.utils.POKEMON_API_STARTING_INDEX
import com.example.accenturechallenge.utils.POKEMON_PAGE_SIZE
import retrofit2.HttpException
import java.io.IOException
import java.io.InvalidObjectException

/**
 * RemoteMediator class works to remove network dependencies when paginating from API
 * Used with Paging 3.0
 */
@ExperimentalPagingApi
class PokemonRemoteMediator(
    private val pokemonClient: PokemonClient,
    private val database: AppDatabase,
    private val apiMapper: ApiMapper
) : RemoteMediator<Int, DbPokemonWithOrWithoutFavorites>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, DbPokemonWithOrWithoutFavorites>
    ): MediatorResult {
        val page = when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(POKEMON_PAGE_SIZE) ?: POKEMON_API_STARTING_INDEX
            }
            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyForFirstItem(state) ?: throw InvalidObjectException("Remote key and the prevKey should not be null")
                val prevKey = remoteKeys.prevKey ?: return MediatorResult.Success(endOfPaginationReached = true)
                remoteKeys.prevKey

            }

            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                if (remoteKeys?.nextKey == null) { throw InvalidObjectException("Remote key should not be null for $loadType") }
                remoteKeys.nextKey
            }
        }


        try {
            val apiResponse = pokemonClient.fetchPokemons(
                offset = page,
                itemsPerPage = state.config.pageSize
            )

//            val apiFilteredResponse = apiResponse.resourceResults.filter { it.name.toLowerCase(Locale.ENGLISH).contains(query.toLowerCase(Locale.ENGLISH)) }
            val apiFilteredResponse = apiResponse.resourceResults
            val pokemons = apiFilteredResponse.map { apiMapper.mapApiPokemonToModel(it) }
            val endOfPaginationReached = apiResponse.next == null
            database.withTransaction {

                if (loadType == LoadType.REFRESH) {
                    database.remoteKeysDao().clearRemoteKeys()
                    database.pokemonDao().clearAllPokemons()
                }
                val prevKey =
                    if (page == POKEMON_API_STARTING_INDEX) null else page - POKEMON_PAGE_SIZE
                val nextKey = if (endOfPaginationReached) null else page + POKEMON_PAGE_SIZE
                val keys = pokemons.map {
                    DbRemoteKeys(id = it.id, prevKey = prevKey, nextKey = nextKey)
                }
                database.remoteKeysDao().insertAll(keys)
                database.pokemonDao().insertAllPokemons(pokemons)
            }
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (exception: IOException) {
            return MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            return MediatorResult.Error(exception)
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, DbPokemonWithOrWithoutFavorites>): DbRemoteKeys? {
        // Get the last page that was retrieved, that contained items.
        // From that last page, get the last item
        return state.pages.lastOrNull() { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { pokemonFavorite ->
                // Get the remote keys of the last item retrieved
                database.remoteKeysDao().remoteKeysPokemonId(pokemonFavorite.pokemon.id)
            }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, DbPokemonWithOrWithoutFavorites>): DbRemoteKeys? {
        // Get the first page that was retrieved, that contained items.
        // From that first page, get the first item
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { pokemonFavorite ->
                // Get the remote keys of the first items retrieved
                database.remoteKeysDao().remoteKeysPokemonId(pokemonFavorite.pokemon.id)
            }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, DbPokemonWithOrWithoutFavorites>
    ): DbRemoteKeys? {
        // The paging library is trying to load data after the anchor position
        // Get the item closest to the anchor position
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.pokemon?.id?.let { pokemonFavoriteId ->
                database.remoteKeysDao().remoteKeysPokemonId(pokemonFavoriteId)
            }
        }
    }


}