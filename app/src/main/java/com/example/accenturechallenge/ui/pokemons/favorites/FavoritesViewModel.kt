package com.example.accenturechallenge.ui.pokemons.favorites

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.accenturechallenge.data.database.entities.DbPokemon
import com.example.accenturechallenge.data.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    private val _favoritePokemons = MutableLiveData<List<DbPokemon>>()
    fun getFavoritePokemons(): LiveData<List<DbPokemon>> = _favoritePokemons

    init {
        viewModelScope.launch {
            val favorites = repository.fetchFavoritePokemons()
            _favoritePokemons.postValue(favorites)
        }

    }

}
