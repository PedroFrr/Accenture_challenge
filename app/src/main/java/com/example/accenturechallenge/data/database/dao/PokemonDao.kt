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

//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun favoritePokemon(favorite: DbFavorite)

//    @DELETE
//    suspend fun remoteFavoritePokemon(favorite: DbFavorite)


    @Query("UPDATE pokemon SET isFavorite =  NOT isFavorite WHERE id = :pokemonId")
    suspend fun favoritePokemon(pokemonId: String)


//    @Transaction
//    @Query("SELECT * FROM pokemon")
//    fun fetchFavoritePokemons(): List<DbPokemonFavorites>


    @Query("SELECT * FROM pokemon WHERE isFavorite = 1")
    fun fetchFavoritePokemons(): Flow<List<DbPokemon>>

    @Query("SELECT * FROM pokemon WHERE id = :pokemonId LIMIT 1")
    fun fetchPokemonDetail(pokemonId: String): Flow<DbPokemon>


    /**
     * Ref Data operations
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllAbilitiesRef(abilities: List<DbPokemonAbility>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllTypesRef(abilities: List<DbPokemonType>)


    /**
     * Many to Many queries
     */
    @Transaction
    @Query("SELECT * FROM pokemon_detail WHERE pokemonDetailId = :pokemonId")
    fun getPokemonWithAbilities(pokemonId: String): DbPokemonDetailWithAbilities

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPokemonWithAbility(pokemonAbilityCrossRef: DbPokemonAbilityCrossRef)



}