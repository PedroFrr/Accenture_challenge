package com.example.accenturechallenge.data.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.accenturechallenge.data.database.entities.DbPokemon

@Dao
interface PokemonDao {

    //OnConflicted is set to IGNORE as to not replace pokemons already downloaded (and possibly favorited by the user)
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(doggoModel: List<DbPokemon>)

    @Query("SELECT * FROM pokemon")
    fun fetchAllPokemons(): PagingSource<Int, DbPokemon>

    @Query("DELETE FROM pokemon")
    suspend fun clearAllPokemons()

    @Query("UPDATE pokemon SET isFavorite =  NOT isFavorite WHERE id = :pokemonId")
    suspend fun favoritePokemon(pokemonId: String)

    @Query("SELECT * FROM pokemon WHERE isFavorite = 1")
    suspend fun fetchFavoritePokemons(): List<DbPokemon>

}