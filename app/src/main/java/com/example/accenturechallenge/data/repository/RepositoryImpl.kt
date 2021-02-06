package com.example.accenturechallenge.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.accenturechallenge.data.*
import com.example.accenturechallenge.data.database.AppDatabase
import com.example.accenturechallenge.data.database.entities.*
import com.example.accenturechallenge.data.network.pokemonapi.PokemonClient
import com.example.accenturechallenge.data.network.pokemonapi.mapper.ApiMapper
import com.example.accenturechallenge.data.network.pokemonapi.response.GetResourceResponse
import com.example.accenturechallenge.data.network.webhook.WebhookService
import com.example.accenturechallenge.data.network.webhook.request.FavoritePokemonRequest
import com.example.accenturechallenge.utils.POKEMON_PAGE_SIZE
import kotlinx.coroutines.flow.*
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
    override fun fetchPokemons(): Flow<PagingData<DbPokemonWithOrWithoutFavorites>> {

        return Pager(
            config = PagingConfig(
                pageSize = POKEMON_PAGE_SIZE,
                enablePlaceholders = true
            ),
            //TODO change query
            remoteMediator = PokemonRemoteMediator(
                "",
                pokemonClient,
                database,
                apiMapper
            ),
            pagingSourceFactory = { database.pokemonDao().getPokemonWithFavorites() }

        ).flow


    }

    override fun fetchFavoritePokemons(): Flow<List<DbPokemonWithOrWithoutFavorites>> = database.pokemonDao().fetchFavoritePokemons().map { it.filter { item -> item.favorite != null } }

    override suspend fun favoritePokemon(pokemonWithOrWithoutWithFavorite: DbPokemonWithOrWithoutFavorites) {

       /*
       Favorites pokemon if it's not yet a Favorite
       Unfavorites it otherwise
        */
        if(pokemonWithOrWithoutWithFavorite.favorite == null){
            database.pokemonDao().favoritePokemon(DbFavorite(pokemonWithOrWithoutWithFavorite.pokemon.id))
        }else{
            database.pokemonDao().unfavoritePokemon(DbFavorite(pokemonWithOrWithoutWithFavorite.pokemon.id))
        }

        /*
        Sends POST request to Webhook service
         */
        val request = FavoritePokemonRequest(
            id = pokemonWithOrWithoutWithFavorite.pokemon.id,
            name = pokemonWithOrWithoutWithFavorite.pokemon.name,
            timestamp = System.currentTimeMillis(),
            wasFavorite = pokemonWithOrWithoutWithFavorite.favorite == null
        )
        webhookService.postFavorite(request)


    }

    override fun fetchPokemonDetailWithAbilitiesAndTypes(pokemonId: String): Flow<Result<DbPokemonWithAbilitiesAndTypes>> {
        return flow {
            emit(Loading)
            val result = pokemonClient.fetchPokemonDetail(pokemonId)

            //If couldn't retrieve from network, return from DB
            //If Db is empty, return Failure to UI layer
            if (result is Failure) {
                val pokemonWithAbilitiesAndTypes = database.pokemonDao().getPokemonWithAbilitiesAndTypes(pokemonId)
                if(pokemonWithAbilitiesAndTypes != null){
                    emit(Success(pokemonWithAbilitiesAndTypes))
                }else{
                    emit(Failure(result.error))
                }
            } else {
                val pokemonResult = result as Success

                val pokemonDetail = apiMapper.mapApiPokemonDetailToModel(pokemonResult.data)

                /**
                 * Transform each ability/type into a CrossRef relation between the Pokemon Detail and the abilities/Types (M to M)
                 */

                val pokemonAbilitiesCrossRef = pokemonResult.data.abilities
                    .map { apiMapper.mapApiAbilityToPokemonAbility(it) }
                    .map { DbPokemonAbilityCrossRef(
                        pokemonDetailId = pokemonId,
                        abilityId = it.abilityId,
                    ) }

                val pokemonTypesCrossRef = pokemonResult.data.types
                    .map { apiMapper.mapApiTypeToPokemonType(it) }
                    .map { DbPokemonTypeCrossRef(
                        pokemonDetailId = pokemonId,
                        typeId = it.typeId,
                    ) }


                //Update cache (offline first) with Pokemon Detail and respective abilities
                //As to then query the Db for PokemonWithAbilities
                database.pokemonDao().insertPokemonWithAbilities(pokemonAbilitiesCrossRef)
                database.pokemonDao().insertPokemonWithTypes(pokemonTypesCrossRef)
                database.pokemonDao().insertPokemonDetail(pokemonDetail)

                val pokemonWithAbilitiesAndTypes = database.pokemonDao().getPokemonWithAbilitiesAndTypes(pokemonId)
                if(pokemonWithAbilitiesAndTypes != null){
                    emit(Success(pokemonWithAbilitiesAndTypes))
                }else{
                    emit(Failure(NullPointerException("No data")))
                }

            }
        }
    }

    override fun getDbPokemonDetail(pokemonId: String): Flow<DbPokemonWithOrWithoutFavorites> = database.pokemonDao().fetchPokemonDetail(pokemonId)

    override suspend fun fetchPokemonAbilities(): Result<GetResourceResponse> = pokemonClient.fetchPokemonAbilities()

    override suspend fun fetchPokemonTypes(): Result<GetResourceResponse> = pokemonClient.fetchPokemonTypes()

}
