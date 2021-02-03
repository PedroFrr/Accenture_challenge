package com.example.accenturechallenge.ui.pokemons.list

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.accenturechallenge.R
import com.example.accenturechallenge.databinding.FragmentPokemonListBinding
import com.example.accenturechallenge.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/** [Fragment] class to represent cat breed list.
 * ViewBinding is done using an extension available online. Allows for view binding with one single line
 */

@AndroidEntryPoint
class PokemonListFragment : Fragment(R.layout.fragment_pokemon_list) {
    private val binding by viewBinding(FragmentPokemonListBinding::bind)
    private val pokemonListViewModel: PokemonListViewModel by viewModels()
    private val pokemonListAdapter by lazy { PokemonListPagingDataAdapter() }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fetchPokemons()

        initUi()
    }

    private fun initUi(){

        initAdapter()
    }

    private fun initAdapter(){
        binding.pokemonRecyclerView.apply {
            adapter = pokemonListAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
            hasFixedSize()
        }
    }

    private fun fetchPokemons() {
        lifecycleScope.launch {
            pokemonListViewModel.fetchPokemons().collectLatest { pagingData ->
                pokemonListAdapter.submitData(pagingData)
            }
        }
    }


}