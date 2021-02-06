package com.example.accenturechallenge.ui.pokemons.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.accenturechallenge.data.database.entities.DbPokemonWithOrWithoutFavorites
import com.example.accenturechallenge.databinding.FragmentFavoritesBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass.
 */
@AndroidEntryPoint
class FavoritesFragment : Fragment() {
//    private val binding by viewBinding(FragmentFavoritesBinding::bind)

    private var _binding: FragmentFavoritesBinding? = null
    private val favoritesViewModel: FavoritesViewModel by viewModels()
    private val pokemonListAdapter by lazy { FavoritesAdapter(::onFavoritePokemon) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUi()
        initObservables()
    }

    private fun initUi() {
        initAdapter()
    }

    private fun initAdapter() {
        _binding?.pokemonRecyclerView?.apply {
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

    private fun onFavoritePokemon(pokemonWithFavorites: DbPokemonWithOrWithoutFavorites){
        favoritesViewModel.favoritePokemon(pokemonWithFavorites)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}