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
import androidx.recyclerview.widget.GridLayoutManager
import com.example.accenturechallenge.R
import com.example.accenturechallenge.data.database.entities.DbPokemonWithOrWithoutFavorites
import com.example.accenturechallenge.databinding.FragmentPokemonListBinding
import com.example.accenturechallenge.utils.gone
import com.example.accenturechallenge.utils.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/** [Fragment] class to represent cat breed list.
 * ViewBinding is done using an extension available online. Allows for view binding with one single line
 */

@AndroidEntryPoint
class PokemonListFragment : Fragment() {

    private var _binding: FragmentPokemonListBinding? = null
    private val pokemonListViewModel: PokemonListViewModel by viewModels()
    private val pokemonListAdapter by lazy { PokemonListPagingDataAdapter(::favoritePokemon) }

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
        //GridColumnCount represents the number of columns to display on the layout
        //In portrait mode -> 1 column; in landscape -> 2 columns
        val gridColumnCount = resources.getInteger(R.integer.grid_column_count)
        _binding?.pokemonRecyclerView?.apply {
            layoutManager = GridLayoutManager(requireContext(), gridColumnCount)
            adapter = pokemonListAdapter.withLoadStateHeaderAndFooter(
                header = PokemonsLoadStateAdapter { pokemonListAdapter.retry() },
                footer = PokemonsLoadStateAdapter { pokemonListAdapter.retry() })
            hasFixedSize()
        }

        pokemonListAdapter.addLoadStateListener { loadState ->

//            //TODO revisit some ticks on app
//            if (loadState.source.refresh is LoadState.Loading) {
//                _binding?.progressBar?.visible()
//            } else {
//                _binding?.progressBar?.gone()
//
//            }

            // Show the retry state if initial load or refresh fails.
            when(loadState.refresh){
                is LoadState.Error -> {
                    _binding?.retryButton?.visible()
                    _binding?.pokemonRecyclerView?.gone()
                    _binding?.progressBar?.gone()
                }
                is LoadState.Loading -> {
                    _binding?.retryButton?.gone()
                    _binding?.pokemonRecyclerView?.gone()
                    _binding?.progressBar?.visible()
                }
                is LoadState.NotLoading -> {
                    _binding?.retryButton?.gone()
                    _binding?.pokemonRecyclerView?.visible()
                    _binding?.progressBar?.gone()
                }
            }



        }


    }

    private fun fetchPokemons() {

        lifecycleScope.launch {
            pokemonListViewModel.fetchPokemons()
                .collectLatest { pagingData ->
                    pokemonListAdapter.submitData(pagingData)
                }
        }

        lifecycleScope.launch {
            pokemonListAdapter.loadStateFlow
                // Only emit when REFRESH LoadState for RemoteMediator changes.
                .distinctUntilChangedBy { it.refresh }
                // Only react to cases where Remote REFRESH completes i.e., NotLoading.
                .filter {it.refresh is LoadState.NotLoading }
                .collectLatest {
                    _binding?.pokemonRecyclerView?.visible()
                    _binding?.progressBar?.gone()
                    _binding?.pokemonRecyclerView?.scrollToPosition(0)
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