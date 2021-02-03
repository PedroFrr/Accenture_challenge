package com.example.accenturechallenge.data.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.accenturechallenge.data.database.entities.DbPokemon

@Dao
interface PokemonDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(doggoModel: List<DbPokemon>)

    @Query("SELECT * FROM pokemon")
    fun fetchAllPokemons(): PagingSource<Int, DbPokemon>

    @Query("DELETE FROM pokemon")
    suspend fun clearAllPokemons()

}