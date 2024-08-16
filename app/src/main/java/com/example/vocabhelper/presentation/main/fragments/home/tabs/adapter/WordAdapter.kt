package com.example.vocabhelper.presentation.main.fragments.home.tabs.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.vocabhelper.data.database.WordEntity
import com.example.vocabhelper.databinding.ItemWordBinding

class WordAdapter(private val onWordClick: (WordEntity) -> Unit) : RecyclerView.Adapter<WordAdapter.WordViewHolder>()
{
    private var words: List<WordEntity> = emptyList()

    class WordViewHolder(val binding: ItemWordBinding) : RecyclerView.ViewHolder(binding.root) {
        val word = binding.wordTV
        val phonetic = binding.phoneticTV
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordAdapter.WordViewHolder {
        val binding = ItemWordBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WordViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WordAdapter.WordViewHolder, position: Int) {
        val word = words[position]
        holder.binding.apply {
            wordTV.text = word.word
            phoneticTV.text = word.phonetic

            root.setOnClickListener {
                onWordClick(word)
            }
        }
    }

    override fun getItemCount() = words.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateWords(newWords: List<WordEntity>) {
        words = newWords
        notifyDataSetChanged()
    }
}