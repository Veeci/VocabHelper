package com.veeci.vocabhelper.presentation.main.fragments.search.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.veeci.vocabhelper.data.models.DefinitionsItem
import com.veeci.vocabhelper.databinding.ItemDefinitionSearchBinding

class DefinitionSearchAdapter: RecyclerView.Adapter<DefinitionSearchAdapter.DefinitionViewHolder>()
{
    private var definiions: List<DefinitionsItem> = listOf()

    class DefinitionViewHolder(val binding: ItemDefinitionSearchBinding) : RecyclerView.ViewHolder(binding.root)
    {
        val definition = binding.definition
        val example = binding.example
        val synonyms = binding.synonyms
        val antonyms = binding.antonyms
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DefinitionSearchAdapter.DefinitionViewHolder {
        val binding = ItemDefinitionSearchBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DefinitionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DefinitionSearchAdapter.DefinitionViewHolder, position: Int) {
        val definitionItem = definiions[position]
        holder.binding.apply {
            definition.text = definitionItem.definition

            if(synonyms.text.isEmpty())
            {
                synonyms.isVisible = false
            }
            else
            {
                synonyms.isVisible = true
                synonyms.text = definitionItem.synonyms?.joinToString(", ")
            }

            if(antonyms.text.isEmpty())
            {
                antonyms.isVisible = false
            }
            else
            {
                antonyms.isVisible = true
                antonyms.text = definitionItem.antonyms?.joinToString(", ") ?: "None"
            }

            if(example.text.isEmpty())
            {
                example.isVisible = false
            }
            else
            {
                example.isVisible = true
                example.text = definitionItem.example ?: "None"
            }
        }
    }

    override fun getItemCount() = definiions.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateDefinitions(definitions: List<DefinitionsItem>) {
        this.definiions = definitions
        notifyDataSetChanged()
    }
}