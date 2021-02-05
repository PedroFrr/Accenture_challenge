package com.example.accenturechallenge.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.work.*
import com.example.accenturechallenge.data.database.dao.PokemonDao
import com.example.accenturechallenge.data.database.dao.RemoteKeysDao
import com.example.accenturechallenge.data.database.entities.*
import com.example.accenturechallenge.utils.DATABASE_NAME
import com.example.accenturechallenge.workers.PokemonAbilityRefDataWorker
import com.example.accenturechallenge.workers.PokemonTypeRefDataWorker
import java.util.concurrent.TimeUnit

/**
 * SQLite Database for storing the Pokemon API results
 */
@Database(
    entities = [DbPokemon::class, DbRemoteKeys::class, DbPokemonDetail::class, DbPokemonAbility::class, DbPokemonAbilityCrossRef::class, DbPokemonType::class, DbPokemonTypeCrossRef::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun pokemonDao(): PokemonDao
    abstract fun remoteKeysDao(): RemoteKeysDao

    companion object {

        @Volatile
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME)
                /**
                 * Will populate the Db (Pokemon Ability and Type on the first time it loads)
                 */
                .addCallback(
                    object : RoomDatabase.Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)

                            //The Workers defined below won't start until these conditions are met (they are guaranteed to start even if they're only met in the future)
                            val constraints = Constraints.Builder()
                                .setRequiredNetworkType(NetworkType.UNMETERED)
                                .setRequiresStorageNotLow(true)
                                .build()

                            //the backoff criteria is set to retry the worker in case it fails. Linear is set so the retry interval gradually increases (if it keeps failing)
                            val pokemonAbilityRefDataWorker =
                                OneTimeWorkRequestBuilder<PokemonAbilityRefDataWorker>()
                                    .setConstraints(constraints)
                                    .setBackoffCriteria(
                                        BackoffPolicy.LINEAR,
                                        OneTimeWorkRequest.MIN_BACKOFF_MILLIS,
                                        TimeUnit.MILLISECONDS
                                    )
                                    .build()

                            val pokemonTypeRefDataWorker =
                                OneTimeWorkRequestBuilder<PokemonTypeRefDataWorker>()
                                    .setConstraints(constraints)
                                    .setBackoffCriteria(
                                        BackoffPolicy.LINEAR,
                                        OneTimeWorkRequest.MIN_BACKOFF_MILLIS,
                                        TimeUnit.MILLISECONDS
                                    )
                                    .build()

                            WorkManager.getInstance(context)
                                .beginWith(
                                    listOf(
                                        pokemonAbilityRefDataWorker,
                                        pokemonTypeRefDataWorker
                                    )
                                )
                                .enqueue()

                        }
                    }
                )
                .build()
        }
    }
}