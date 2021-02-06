package com.example.accenturechallenge.data.database.dao

import androidx.paging.PagingSource
import androidx.room.*
import com.example.accenturechallenge.data.database.entities.*
import kotlinx.coroutines.flow.Flow

@Dao
interface PokemonDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllPokemons(pokemons: List<DbPokemon>)

    @Query("SELECT * FROM pokemon")
    fun fetchAllPokemons(): PagingSource<Int, DbPokemon>

    @Query("DELETE FROM pokemon")
    suspend fun clearAllPokemons()

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun favoritePokemon(pokemonFavorite: DbFavorite)

    @Delete
    suspend fun unfavoritePokemon(pokemonFavorite: DbFavorite)

    @Query("SELECT * FROM pokemon ")
    fun fetchFavoritePokemons(): Flow<List<DbPokemonWithOrWithoutFavorites>>

    @Query("SELECT * FROM pokemon WHERE id = :pokemonId LIMIT 1")
    fun fetchPokemonDetail(pokemonId: String): Flow<DbPokemonWithOrWithoutFavorites>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPokemonDetail(pokemonDetail: DbPokemonDetail)

    @Query("SELECT * FROM pokemon")
    suspend fun observeAllPokemons(): List<DbPokemon>


    /**
     * Ref Data operations
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllAbilitiesRef(abilities: List<DbPokemonAbility>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllTypesRef(abilities: List<DbPokemonType>)

    /**
     * One to One queries and relationships
     */
    @Transaction
    @Query("SELECT * FROM pokemon")
    fun getPokemonWithFavorites(): PagingSource<Int, DbPokemonWithOrWithoutFavorites>

    /**
     * Many to Many queries and relationships
     */

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPokemonWithAbilities(pokemonAbilityCrossRef: List<DbPokemonAbilityCrossRef>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPokemonWithTypes(pokemonTypeCrossRef: List<DbPokemonTypeCrossRef>)

    @Transaction
    @Query("SELECT * FROM pokemon_detail WHERE pokemonDetailId = :pokemonId")
    suspend fun getPokemonWithAbilitiesAndTypes(pokemonId: String): DbPokemonWithAbilitiesAndTypes?


}