package com.example.vocabhelper.presentation.main.fragments.home.tabs.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.vocabhelper.databinding.ItemWordBinding

class WordAdapter : RecyclerView.Adapter<WordAdapter.WordViewHolder>()
{

    class WordViewHolder(val binding: ItemWordBinding) : RecyclerView.ViewHolder(binding.root) {
        val word = binding.wordTV
        val phonetic = binding.phoneticTV
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordAdapter.WordViewHolder {
        val binding = ItemWordBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WordViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WordAdapter.WordViewHolder, position: Int) {

    }

    override fun getItemCount(): Int {
        return 0
    }
}