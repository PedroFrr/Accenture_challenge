package com.example.accenturechallenge

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.filters.SmallTest
import com.example.accenturechallenge.data.database.AppDatabase
import com.example.accenturechallenge.data.database.dao.PokemonDao
import com.example.accenturechallenge.data.database.entities.DbPokemon
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject
import javax.inject.Named

@SmallTest
@HiltAndroidTest //Tells Hilt that we want to inject dependencies into this Test class
class PokemonDaoTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    @Named("test_db")
    lateinit var database: AppDatabase
    private lateinit var pokemonDao: PokemonDao

    @Before
    fun setup(){
        hiltRule.inject()
        pokemonDao = database.pokemonDao()
    }

    @After
    fun teardown() {
        database.close()
    }

    @ExperimentalCoroutinesApi
    @Test
    fun insertPokemon() = runBlockingTest {

        val pokemon1 = DbPokemon(id = "1", name = "Bulbasaur", url = "Mock_URL")
        val pokemon2 = DbPokemon(id = "2", name = "Bulbasaur", url = "Mock_URL")
        val pokemon3 = DbPokemon(id = "3", name = "Bulbasaur", url = "Mock_URL")
        val pokemons = listOf<DbPokemon>(
            pokemon1,
            pokemon2
        )

        pokemonDao.insertAllPokemons(pokemons)

        val allPokemons = pokemonDao.observeAllPokemons()

        assertThat(allPokemons).contains(pokemon1)
        assertThat(allPokemons).contains(pokemon2)
        assertThat(allPokemons).doesNotContain(pokemon3)
    }

}