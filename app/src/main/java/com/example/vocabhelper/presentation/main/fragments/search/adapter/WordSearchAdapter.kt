package com.example.vocabhelper.presentation.main.fragments.search.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.marginTop
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.vocabhelper.data.models.MeaningsItem
import com.example.vocabhelper.databinding.ItemWordSearchBinding

class WordSearchAdapter: RecyclerView.Adapter<WordSearchAdapter.WordViewHolder>()
{
    private var meanings: List<MeaningsItem> = emptyList()

    class WordViewHolder(val binding: ItemWordSearchBinding) : RecyclerView.ViewHolder(binding.root)
    {
        val partOfSpeech = binding.partOfSpeech
        val synonyms = binding.synonyms
        val antonyms = binding.antonyms
        val definition = binding.definitionRecyclerView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordViewHolder {
        val binding = ItemWordSearchBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WordViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WordViewHolder, position: Int) {
        val meaning = meanings[position]
        holder.binding.apply {
            partOfSpeech.text = meaning.partOfSpeech
            synonyms.text = ("synonyms: " + meaning.synonyms?.joinToString(", "))
            antonyms.text = ("antonyms: " + meaning.antonyms?.joinToString(", "))

            // Set up the Definition adapter
            val definitionAdapter = DefinitionSearchAdapter()
            definitionRecyclerView.apply {
                layoutManager = LinearLayoutManager(holder.itemView.context)
                adapter = definitionAdapter
            }

            meaning.definitions?.let { definitions ->
                definitionAdapter.updateDefinitions(definitions.filterNotNull())
            }
        }
    }


    override fun getItemCount() = meanings.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateMeanings(newMeanings: List<MeaningsItem>) {
        meanings = newMeanings
        notifyDataSetChanged()
    }
}