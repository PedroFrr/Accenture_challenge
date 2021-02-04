package com.example.accenturechallenge.ui.pokemons.detail

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.accenturechallenge.R
import com.example.accenturechallenge.databinding.FragmentPokemonDetailBinding
import com.example.accenturechallenge.utils.loadImage
import com.example.accenturechallenge.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 * A simple [Fragment] subclass.
 */

@AndroidEntryPoint
class PokemonDetailFragment : Fragment(R.layout.fragment_pokemon_detail) {
    private val binding by viewBinding(FragmentPokemonDetailBinding::bind)
    private val pokemonDetailViewModel: PokemonDetailViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUi()
        initObservables()
    }

    private fun initUi(){
        arguments?.let {
            val args = PokemonDetailFragmentArgs.fromBundle(it)
            val pokemonId = args.pokemonId
            pokemonDetailViewModel.fetchPokemonDetail(pokemonId)

        }

    }

    private fun initObservables(){
        pokemonDetailViewModel.getPokemonDetail().observe(viewLifecycleOwner, { pokemon ->
            binding.imageViewDetail.loadImage(pokemon.url)
        })
    }


}