package com.example.accenturechallenge.data.database.dao

import androidx.paging.PagingSource
import androidx.room.*
import com.example.accenturechallenge.data.database.entities.*
import kotlinx.coroutines.flow.Flow

@Dao
interface PokemonDao {

    //OnConflicted is set to IGNORE as to not replace pokemons already downloaded (and possibly favorited by the user)
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAllPokemons(pokemons: List<DbPokemon>)

    @Query("SELECT * FROM pokemon")
    fun fetchAllPokemons(): PagingSource<Int, DbPokemon>

    @Query("DELETE FROM pokemon")
    suspend fun clearAllPokemons()

    @Query("UPDATE pokemon SET isFavorite =  NOT isFavorite WHERE id = :pokemonId")
    suspend fun favoritePokemon(pokemonId: String)

    @Query("SELECT * FROM pokemon WHERE isFavorite = 1")
    fun fetchFavoritePokemons(): Flow<List<DbPokemon>>

    @Query("SELECT * FROM pokemon WHERE id = :pokemonId LIMIT 1")
    fun fetchPokemonDetail(pokemonId: String): Flow<DbPokemon>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPokemonDetail(pokemonDetail: DbPokemonDetail)


    /**
     * Ref Data operations
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllAbilitiesRef(abilities: List<DbPokemonAbility>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllTypesRef(abilities: List<DbPokemonType>)

    /**
     * Many to Many queries and relationships
     */

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPokemonWithAbilities(pokemonAbilityCrossRef: List<DbPokemonAbilityCrossRef>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPokemonWithTypes(pokemonTypeCrossRef: List<DbPokemonTypeCrossRef>)

    @Query("SELECT * FROM pokemon_detail WHERE pokemonDetailId = :pokemonId")
    suspend fun getPokemonWithAbilitiesAndTypes(pokemonId: String): DbPokemonWithAbilitiesAndTypes?


}