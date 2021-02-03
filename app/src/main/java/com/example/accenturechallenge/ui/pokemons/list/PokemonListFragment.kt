package com.example.accenturechallenge.ui.pokemons.list

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.accenturechallenge.R
import com.example.accenturechallenge.databinding.FragmentPokemonListBinding
import com.example.accenturechallenge.utils.toast
import com.example.accenturechallenge.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/** [Fragment] class to represent cat breed list.
 * ViewBinding is done using an extension available online. Allows for view binding with one single line
 */

@AndroidEntryPoint
class PokemonListFragment : Fragment(R.layout.fragment_pokemon_list) {
    private val binding by viewBinding(FragmentPokemonListBinding::bind)
    private val pokemonListViewModel: PokemonListViewModel by viewModels()
    private val pokemonListAdapter by lazy { PokemonListPagingDataAdapter() }

    private var searchJob: Job? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fetchPokemons()
        initUi()


    }

    private fun initUi() {

//        binding.retryButton.setOnClickListener { pokemonListAdapter.retry() }

        initAdapter()

    }

    private fun initAdapter() {
        binding.pokemonRecyclerView.apply {
            adapter = pokemonListAdapter
            adapter = pokemonListAdapter.withLoadStateHeaderAndFooter(
                header = PokemonsLoadStateAdapter { pokemonListAdapter.retry() },
                footer = PokemonsLoadStateAdapter { pokemonListAdapter.retry() })
            hasFixedSize()
        }


        // add dividers between RecyclerView's row items
        val decoration = DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
        binding.pokemonRecyclerView.addItemDecoration(decoration)

        pokemonListAdapter.addLoadStateListener { loadState ->
            //TODO revisit some ticks on app

            // Show loading spinner during initial load or refresh.
            binding.progressBar.isVisible = loadState.source.refresh is LoadState.Loading
            // Show the retry state if initial load or refresh fails.
            binding.retryButton.isVisible = loadState.source.refresh is LoadState.Error


        }

    }

    private fun fetchPokemons() {
        //TODO only makes sense if I endup with search otherwise delete

        // Make sure we cancel the previous job before creating a new one
        searchJob?.cancel()
        searchJob = lifecycleScope.launch {
            pokemonListViewModel.fetchPokemons().distinctUntilChanged()
                .collectLatest { pagingData ->
                    pokemonListAdapter.submitData(pagingData)
                }
        }

    }


}