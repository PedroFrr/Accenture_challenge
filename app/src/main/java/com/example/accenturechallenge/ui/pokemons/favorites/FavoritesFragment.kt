package com.example.accenturechallenge.ui.pokemons.favorites

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.example.accenturechallenge.R
import com.example.accenturechallenge.databinding.FragmentFavoritesBinding
import com.example.accenturechallenge.ui.pokemons.list.PokemonsLoadStateAdapter
import com.example.accenturechallenge.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass.
 */
@AndroidEntryPoint
class FavoritesFragment : Fragment(R.layout.fragment_favorites) {
    private val binding by viewBinding(FragmentFavoritesBinding::bind)
    private val favoritesViewModel: FavoritesViewModel by viewModels()
    private val pokemonListAdapter by lazy { FavoritesAdapter() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUi()
        initObservables()
    }

    private fun initUi() {
        initAdapter()
    }

    private fun initAdapter() {
        binding.pokemonRecyclerView.apply {
            adapter = pokemonListAdapter
            hasFixedSize()
        }

    }

    private fun initObservables() {
        lifecycleScope.launch {
            favoritesViewModel.getFavoritePokemons().observe(viewLifecycleOwner, { pokemons ->
                pokemonListAdapter.submitList(pokemons)
            })
        }
    }

}