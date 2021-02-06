package com.example.accenturechallenge.ui.pokemons.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.accenturechallenge.R
import com.example.accenturechallenge.data.Failure
import com.example.accenturechallenge.data.Loading
import com.example.accenturechallenge.data.Success
import com.example.accenturechallenge.databinding.FragmentPokemonDetailBinding
import com.example.accenturechallenge.utils.POKEMON_IMAGE_BASE_URL
import com.example.accenturechallenge.utils.gone
import com.example.accenturechallenge.utils.loadImage
import com.example.accenturechallenge.utils.visible
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

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
    ): View {
        _binding = FragmentPokemonDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUi()
        initObservables()
    }

    private fun initUi() {
        arguments?.let {
            val args = PokemonDetailFragmentArgs.fromBundle(it)
            val pokemonId = args.pokemonId
            pokemonDetailViewModel.fetchPokemonDetail(pokemonId)
            pokemonDetailViewModel.getDbPokemon(pokemonId)

        }

    }

    //TODO change this method
    private fun initObservables() {
        /**
         * Observe response from API, ideally we would merge Pokemon API response with DB
         */
        pokemonDetailViewModel.getPokemonDetail().observe(viewLifecycleOwner, { result ->
            when (result) {

                is Success -> {
                    binding.pokemonDetailAppBar.appbar.visible()
                    binding.pokemonContent.visible()
                    binding.progressBar.gone()
                    binding.errorMessage.gone()

                    val pokemonDetail = result.data

                    binding.apply {
//                        toolbar.title = pokemonDetail.name //TODO see if it should stay
                        pokemonName.text = pokemonDetail.pokemon.name
                        height.text = pokemonDetail.pokemon.weight
                        weight.text = pokemonDetail.pokemon.height
                        baseExperience.text = pokemonDetail.pokemon.baseExperience
                        pokemonDetailAppBar.imageViewDetail.loadImage("$POKEMON_IMAGE_BASE_URL${pokemonDetail.pokemon.pokemonDetailId}.png")

                        isFavouriteDetail.setOnClickListener {
                            pokemonDetailViewModel.favoritePokemon()
                        }

                        setChipGroupChips(pokemonDetail.types.map { it.name }, chipsType)
                        setChipGroupChips(pokemonDetail.abilities.map { it.name }, chipsAbility)
                    }

                }
                is Failure -> {
                    binding.errorMessage.visible()
                    binding.pokemonDetailAppBar.appbar.gone()
                    binding.pokemonContent.gone()
                    binding.progressBar.gone()
                }
                Loading -> {
                    binding.progressBar.visible()
                    binding.pokemonContent.gone()
                    binding.errorMessage.gone()
                    binding.pokemonDetailAppBar.appbar.gone()
                }
            }

        })

        /**
         * Observe changes to isFavorite from DB
         */

        pokemonDetailViewModel.getDbPokemon().observe(viewLifecycleOwner, { pokemonWithOrWithoutFavorites ->
            val favoriteDrawable = if (pokemonWithOrWithoutFavorites.favorite != null) R.drawable.ic_baseline_favorite_24 else R.drawable.ic_baseline_favorite_border_24
            binding.isFavouriteDetail.setImageResource(favoriteDrawable)
        })

    }

    //Adds all chips to the corresponding ChipGroup
    private fun setChipGroupChips(chipNames: List<String>, chipGroup: ChipGroup) {
        chipNames.forEach { name ->
            val chip = layoutInflater.inflate(R.layout.item_chip_type, chipGroup, false) as Chip
            chip.text = name
            chip.setChipBackgroundColorResource(setColorBasedOnAbility(name))
            chipGroup.addView(chip)
        }
    }

    private fun setColorBasedOnAbility(type: String): Int =

        when (type.toLowerCase(Locale.ROOT)) {
            "grass", "bug" -> R.color.darkTeal
            "fire" -> R.color.darkRed
            "water", "fighting", "normal" -> R.color.darkBlue
            "electric", "psychic" -> R.color.darkYellow
            "poison", "ghost" -> R.color.darkPurple
            "ground", "rock" -> R.color.darkBrown
            "dark" -> R.color.darkBlack
            else -> R.color.darkRed
        }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}