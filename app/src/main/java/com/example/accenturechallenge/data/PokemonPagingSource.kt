package com.example.accenturechallenge.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.accenturechallenge.data.network.PokemonService
import com.example.accenturechallenge.data.network.response.PokemonResponse
import com.example.accenturechallenge.data.network.response.PokemonResult
import retrofit2.HttpException
import java.io.IOException

/**
 * PokeApi provides previous and next keys, which tell the API how its going to fetch the previous and next pages of data
 */
class PokemonPagingSource(private val pokemonService: PokemonService) : PagingSource<String, PokemonResult>() {

    override fun getRefreshKey(state: PagingState<String, PokemonResult>): String? {
        TODO("Not yet implemented")
    }

    override suspend fun load(params: LoadParams<String>): LoadResult<String, PokemonResult> {
        return try {
            val response = pokemonService.fetchPokemons(itemsPerPage = params.loadSize)

            val pokemons = response.pokemonResults

            LoadResult.Page(
                pokemons ?: listOf(),
                response.previous,
                response.next
            )
        } catch (exception: IOException) { // 6
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }
    }
}