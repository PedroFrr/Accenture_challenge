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
import com.example.accenturechallenge.data.database.entities.DbPokemon
import com.example.accenturechallenge.databinding.FragmentPokemonListBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

/** [Fragment] class to represent cat breed list.
 * ViewBinding is done using an extension available online. Allows for view binding with one single line
 */

@AndroidEntryPoint
class PokemonListFragment : Fragment() {
//    private val binding by viewBinding(FragmentPokemonListBinding::bind)

    private var _binding: FragmentPokemonListBinding? = null
    private val binding get() = _binding!!
    private val pokemonListViewModel: PokemonListViewModel by viewModels()
    private val pokemonListAdapter by lazy { PokemonListPagingDataAdapter(::favoritePokemon) }

    private var searchJob: Job? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPokemonListBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fetchPokemons()
        initUi()

    }

    private fun initUi() {

        binding.retryButton.setOnClickListener { pokemonListAdapter.retry() }

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


        pokemonListAdapter.addLoadStateListener { loadState ->
            //TODO revisit some ticks on app

            // Show loading spinner during initial load or refresh.
            binding.progressBar.isVisible = loadState.refresh is LoadState.Loading
            // Show the retry state if initial load or refresh fails.
            binding.retryButton.isVisible = loadState.source.refresh is LoadState.Error


        }

    }

    private fun fetchPokemons() {
        //TODO job declaration only makes sense if I endup with search otherwise delete

        // Make sure we cancel the previous job before creating a new one
        searchJob?.cancel()
        searchJob = lifecycleScope.launch {
            pokemonListViewModel.fetchPokemons().distinctUntilChanged()
                .collectLatest { pagingData ->
                    pokemonListAdapter.submitData(pagingData)
                }
        }

    }

    private fun favoritePokemon(pokemon: DbPokemon){
        pokemonListViewModel.favoritePokemon(pokemon)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}