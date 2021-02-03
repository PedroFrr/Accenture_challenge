package com.example.accenturechallenge.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.accenturechallenge.data.network.PokemonService
import com.example.accenturechallenge.data.network.response.PokemonResult
import com.example.accenturechallenge.utils.POKEMON_API_STARTING_INDEX
import com.example.accenturechallenge.utils.POKEMON_PAGE_SIZE
import retrofit2.HttpException
import java.io.IOException

/**
 * PokeApi provides previous and next keys, which tell the API how its going to fetch the previous and next pages of data
 */
class PokemonPagingSource(private val pokemonService: PokemonService) : PagingSource<Int, PokemonResult>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PokemonResult> {
        val position = params.key ?: POKEMON_API_STARTING_INDEX

        return try {

            //loadSize at the first request is 3 * pageSize
            val response = pokemonService.fetchPokemons(offset = position, itemsPerPage =  params.loadSize)

            val pokemons = response.pokemonResults

            val nextKey = if (response.next == null) {
                null
            } else {
                position +  POKEMON_PAGE_SIZE


            }
            LoadResult.Page(
                data = pokemons,
                prevKey = if (position == POKEMON_API_STARTING_INDEX) null else position - POKEMON_PAGE_SIZE,
                nextKey = nextKey
            )


        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }
    }

    override val keyReuseSupported: Boolean = true

    override fun getRefreshKey(state: PagingState<Int, PokemonResult>): Int? = null

}