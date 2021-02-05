package com.example.accenturechallenge.ui.pokemons.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.accenturechallenge.R
import com.example.accenturechallenge.databinding.PokemonsLoadStateFooterViewItemBinding
import retrofit2.HttpException
import java.io.IOException

class PokemonsLoadStateAdapter(private val retry: () -> Unit) :
    LoadStateAdapter<PokemonsLoadStateViewHolder>() {
    override fun onBindViewHolder(holder: PokemonsLoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): PokemonsLoadStateViewHolder {
        return PokemonsLoadStateViewHolder.from(parent, retry)
    }
}


class PokemonsLoadStateViewHolder(
    private val binding: PokemonsLoadStateFooterViewItemBinding,
    retry: () -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    init {
        binding.retryButton.setOnClickListener { retry.invoke() }
    }

    fun bind(loadState: LoadState) {
        if (loadState is LoadState.Error) {

            //TODO change to string resource - needs context - if there's time
            //Overwrites default error message
            val errorMessage =
                when (loadState.error) {
                    is HttpException -> "No Connection"
                    is IOException -> "Timeout"
                    else -> null
                }

            binding.errorMsg.text = errorMessage
        }
        binding.progressBar.isVisible = loadState is LoadState.Loading
        binding.retryButton.isVisible = loadState !is LoadState.Loading
        binding.errorMsg.isVisible = loadState !is LoadState.Loading
    }

    companion object {
        fun from(parent: ViewGroup, retry: () -> Unit): PokemonsLoadStateViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.pokemons_load_state_footer_view_item, parent, false)
            val binding = PokemonsLoadStateFooterViewItemBinding.bind(view)
            return PokemonsLoadStateViewHolder(binding, retry)
        }
    }
}
