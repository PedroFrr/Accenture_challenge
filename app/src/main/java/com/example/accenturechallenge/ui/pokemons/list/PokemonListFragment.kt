package com.example.accenturechallenge.ui.pokemons.list

import android.content.res.Configuration
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
import com.example.accenturechallenge.utils.toast
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
            val layout = GridLayoutManager(requireContext(), gridColumnCount)
            layoutManager = layout
            //If orientation is landscape the header has to span on two rows otherwise it just spans on the size of gridColumnCount 
            layout.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    val currentOrientation = resources.configuration.orientation
                    if (currentOrientation == Configuration.ORIENTATION_LANDSCAPE){
                        return if (pokemonListAdapter.getItemViewType(position) == R.layout.separator_pokemon_view_item)  2 else 1
                    }
                    return gridColumnCount
                }
            }
            adapter = pokemonListAdapter.withLoadStateFooter(
                footer = PokemonsLoadStateAdapter { pokemonListAdapter.retry() })
            hasFixedSize()
        }




        pokemonListAdapter.addLoadStateListener { loadState ->

            if (loadState.refresh is LoadState.Loading) {

                _binding?.retryButton?.gone()

                // Show ProgressBar
                _binding?.progressBar?.visible()
            }
            else {
                // Hide ProgressBar
                _binding?.progressBar?.gone()

                // If we have an error, show a toast
                val errorState = when {
                    loadState.append is LoadState.Error -> loadState.append as LoadState.Error
                    loadState.prepend is LoadState.Error -> loadState.prepend as LoadState.Error
                    loadState.refresh is LoadState.Error -> {
                        _binding?.retryButton?.visible()
                        loadState.refresh as LoadState.Error
                    }
                    else -> null
                }
                errorState?.error?.message?.let { toast(it)}
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

    }

    private fun favoritePokemon(pokemonWithOrWithoutWithFavorite: DbPokemonWithOrWithoutFavorites) {
        pokemonListViewModel.favoritePokemon(pokemonWithOrWithoutWithFavorite)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}