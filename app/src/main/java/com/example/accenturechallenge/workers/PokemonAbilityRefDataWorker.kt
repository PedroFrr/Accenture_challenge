package com.example.accenturechallenge.workers

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.accenturechallenge.data.Failure
import com.example.accenturechallenge.data.Success
import com.example.accenturechallenge.data.database.AppDatabase
import com.example.accenturechallenge.data.network.pokemonapi.mapper.ApiMapper
import com.example.accenturechallenge.data.repository.Repository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

/**
 * Saves the Pokemon Abilities on the Db on Database creation
 * [HiltWorker] allows for dependency injection on Workers
 */
@HiltWorker
class PokemonAbilityRefDataWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val repository: Repository,
    private val apiMapper: ApiMapper,
    private val database: AppDatabase
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        val result = repository.fetchPokemonAbilities()

        return if(result is Failure){
            Result.failure()
        }else{
            val pokemonAbilityResource = result as Success
            val pokemonAbilities = pokemonAbilityResource.data.resourceResults.map { apiMapper.mapApiResourceAbilityToPokemonAbility(it) }

            database.pokemonDao().insertAllAbilitiesRef(pokemonAbilities)

            Result.success()


        }
    }
}