package com.example.accenturechallenge.ui.pokemons.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.accenturechallenge.R

class PokemonSeparatorViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val description: TextView = view.findViewById(R.id.separator_description)

    fun bind(separatorText: String) {
        description.text = separatorText
    }

    companion object {
        fun from(parent: ViewGroup): PokemonSeparatorViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.separator_pokemon_view_item, parent, false)
            return PokemonSeparatorViewHolder(view)
        }
    }
}
