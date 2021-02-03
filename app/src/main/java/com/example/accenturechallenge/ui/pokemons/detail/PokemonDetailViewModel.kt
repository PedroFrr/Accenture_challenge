package com.example.accenturechallenge.ui.pokemons.detail

import androidx.lifecycle.ViewModel
import com.example.accenturechallenge.data.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PokemonDetailViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

}