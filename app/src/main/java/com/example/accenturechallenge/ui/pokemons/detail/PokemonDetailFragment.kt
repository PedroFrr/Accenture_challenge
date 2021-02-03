package com.example.accenturechallenge.ui.pokemons.detail

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.accenturechallenge.databinding.FragmentPokemonDetailBinding
import com.example.accenturechallenge.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 * A simple [Fragment] subclass.
 */

@AndroidEntryPoint
class PokemonDetailFragment : Fragment() {
    private val binding by viewBinding(FragmentPokemonDetailBinding::bind)
    private val pokemonDetailViewModel: PokemonDetailViewModel by viewModels()


}