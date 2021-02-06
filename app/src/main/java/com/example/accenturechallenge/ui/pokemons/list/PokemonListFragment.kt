package com.example.accenturechallenge.ui.pokemons.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import com.example.accenturechallenge.data.database.entities.DbPokemonWithOrWithoutFavorites
import com.example.accenturechallenge.databinding.FragmentPokemonListBinding
import com.example.accenturechallenge.utils.gone
import com.example.accenturechallenge.utils.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/** [Fragment] class to represent cat breed list.
 * ViewBinding is done using an extension available online. Allows for view binding with one single line
 */

@AndroidEntryPoint
class PokemonListFragment : Fragment() {
//    private val binding by viewBinding(FragmentPokemonListBinding::bind)

    private var _binding: FragmentPokemonListBinding? = null
    private val pokemonListViewModel: PokemonListViewModel by viewModels()
    private val pokemonListAdapter by lazy { PokemonListPagingDataAdapter(::favoritePokemon) }

    private var searchJob: Job? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPokemonListBinding.inflate(inflater, container, false)
        return _binding?.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        initUi()
        fetchPokemons()

    }

    private fun initUi() {

        _binding?.retryButton?.setOnClickListener { pokemonListAdapter.retry() }

        initAdapter()

    }

    private fun initAdapter() {
        _binding?.pokemonRecyclerView?.apply {
            adapter = pokemonListAdapter
            adapter = pokemonListAdapter.withLoadStateHeaderAndFooter(
                header = PokemonsLoadStateAdapter { pokemonListAdapter.retry() },
                footer = PokemonsLoadStateAdapter { pokemonListAdapter.retry() })
            hasFixedSize()
        }

        pokemonListAdapter.addLoadStateListener { loadState ->

            //TODO revisit some ticks on app
            if (loadState.source.refresh is LoadState.Loading) {
                _binding?.progressBar?.visible()
            } else {
                _binding?.progressBar?.gone()

            }

            // Show the retry state if initial load or refresh fails.
            _binding?.retryButton?.isVisible = loadState.refresh is LoadState.Error


        }

    }

    private fun fetchPokemons() {

        viewLifecycleOwner.lifecycleScope.launch {
            pokemonListViewModel.fetchPokemons()
                .collectLatest { pagingData ->
                    pokemonListAdapter.submitData(pagingData)
                }
        }

    }

    private fun favoritePokemon(pokemonWithOrWithoutWithFavorite: DbPokemonWithOrWithoutFavorites) {
        pokemonListViewModel.favoritePokemon(pokemonWithOrWithoutWithFavorite)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}