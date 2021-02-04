package com.example.accenturechallenge.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.accenturechallenge.data.Failure
import com.example.accenturechallenge.data.PokemonRemoteMediator
import com.example.accenturechallenge.data.Result
import com.example.accenturechallenge.data.Success
import com.example.accenturechallenge.data.database.AppDatabase
import com.example.accenturechallenge.data.database.entities.DbPokemon
import com.example.accenturechallenge.data.network.pokemonapi.PokemonClient
import com.example.accenturechallenge.data.network.pokemonapi.mapper.ApiMapper
import com.example.accenturechallenge.data.network.pokemonapi.response.GetPokemonItemResult
import com.example.accenturechallenge.data.network.webhook.WebhookService
import com.example.accenturechallenge.data.network.webhook.request.FavoritePokemonRequest
import com.example.accenturechallenge.utils.POKEMON_PAGE_SIZE
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val pokemonClient: PokemonClient,
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
                pokemonClient,
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

        //TODO revist
//    override suspend fun fetchPokemonDetail(pokemonId: String): DbPokemonDetailWithAbilities {
//        val result = pokemonClient.fetchPokemonDetail(pokemonId)
//
//        if (result is Failure){
//            Failure(result.error)
//        }else{
//            //TODO change
//            val pokemonDetail = result as Success
//
//            val dbPokemonDetail = DbPokemonDetail(
//                pokemonDetailId = pokemonId,
//                url = "https://pokeres.bastionbot.org/images/pokemon/180.png",
//                name = pokemonDetail.data.name,
//            )
//
//            //Map each ability from API to local DB
//            pokemonDetail.data.abilities
//                .map { DbPokemonAbility(name = it.ability.name) }
//                .forEach {  }
//            val dbAbility = DbPokemonAbility(
//                name = .
//            )
//
//            //Creates relation M to M
//            val pokemonDetailWithAbility = DbPokemonAbilityCrossRef(
//
//            )

//    database.pokemonDao().getPokemonWithAbilities(pokemonId)
//        }

    //TODO for now I'm fetching directly from the API see aboce for another solution by first saving to DB
    override suspend fun fetchPokemonDetail(pokemonId: String): Result<GetPokemonItemResult>{

        val result = pokemonClient.fetchPokemonDetail(pokemonId)

        return if (result is Failure) {
            Failure(result.error)
        }else{
            val pokemonDetail = result as Success
            Success(pokemonDetail.data)
        }


    }


}