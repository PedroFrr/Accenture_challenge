package com.example.accenturechallenge.ui.pokemons.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.accenturechallenge.data.Failure
import com.example.accenturechallenge.data.Loading
import com.example.accenturechallenge.data.Success
import com.example.accenturechallenge.databinding.FragmentPokemonDetailBinding
import com.example.accenturechallenge.utils.POKEMON_IMAGE_BASE_URL
import com.example.accenturechallenge.utils.loadImage
import dagger.hilt.android.AndroidEntryPoint

/**
 * A simple [Fragment] subclass.
 */

@AndroidEntryPoint
class PokemonDetailFragment : Fragment() {
    private var _binding: FragmentPokemonDetailBinding? = null
    private val binding get() = _binding!!

//    private val binding by viewBinding(FragmentPokemonDetailBinding::bind)
    private val pokemonDetailViewModel: PokemonDetailViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPokemonDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

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

    //TODO change this method
    private fun initObservables(){
        pokemonDetailViewModel.getPokemonDetail().observe(viewLifecycleOwner, { result ->
            val regex = "(\\d+)(?!.*\\d)".toRegex()
            when(result){

                is Success -> {

                    val pokemonDetail = result.data
                    binding.imageViewDetail.loadImage("$POKEMON_IMAGE_BASE_URL${pokemonDetail.id}.png")
                }
                is Failure -> TODO()
                Loading -> TODO()
            }



        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }



}