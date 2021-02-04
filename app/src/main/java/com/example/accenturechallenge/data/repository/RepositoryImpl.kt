package com.example.accenturechallenge.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.accenturechallenge.data.PokemonRemoteMediator
import com.example.accenturechallenge.data.database.AppDatabase
import com.example.accenturechallenge.data.database.entities.DbFavorite
import com.example.accenturechallenge.data.database.entities.DbPokemon
import com.example.accenturechallenge.data.database.entities.DbPokemonFavorites
import com.example.accenturechallenge.data.network.pokemonapi.PokemonService
import com.example.accenturechallenge.data.network.pokemonapi.mapper.ApiMapper
import com.example.accenturechallenge.data.network.webhook.WebhookService
import com.example.accenturechallenge.data.network.webhook.request.FavoritePokemonRequest
import com.example.accenturechallenge.utils.POKEMON_PAGE_SIZE
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val pokemonService: PokemonService,
    private val database: AppDatabase,
    private val apiMapper: ApiMapper,
    private val webhookService: WebhookService
) : Repository {

    /**
     * RemoteMediator which keeps a local database cache in the case the user has no internet connection
     */
    @ExperimentalPagingApi
    override fun fetchPokemons(): Flow<PagingData<DbPokemon>> {
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
            pagingSourceFactory = { database.pokemonDao().fetchAllPokemons() }

        ).flow


    }

    override suspend fun fetchFavoritePokemons(): List<DbPokemon> =
        database.pokemonDao().fetchFavoritePokemons()

    override suspend fun favoritePokemon(pokemon: DbPokemon) {
        database.pokemonDao().favoritePokemon(pokemon.id)

        val request = FavoritePokemonRequest(
            id = pokemon.id,
            name = pokemon.name,
            timestamp = System.currentTimeMillis(),
            wasFavorite = pokemon.isFavorite.not()
        )
        webhookService.postFavorite(request)


    }


    override suspend fun fetchPokemonDetail(pokemonId: String): DbPokemon = database.pokemonDao().fetchPokemonDetail(pokemonId)


}