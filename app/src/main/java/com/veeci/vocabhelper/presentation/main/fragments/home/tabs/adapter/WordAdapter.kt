package com.veeci.vocabhelper.presentation.main.fragments.home.tabs.adapter

import android.annotation.SuppressLint
import android.media.MediaPlayer
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.veeci.vocabhelper.data.models.WordData
import com.veeci.vocabhelper.databinding.ItemAddedwordBinding

class WordAdapter(private val onItemClick: (WordData) -> Unit): RecyclerView.Adapter<WordAdapter.WordViewHolder>()
{
    private var words: List<WordData> = listOf()

    private var mediaPlayer: MediaPlayer? = null

    class WordViewHolder(val binding: ItemAddedwordBinding) : RecyclerView.ViewHolder(binding.root) {
        val word = binding.wordAdded
        val meaning = binding.meaning
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordViewHolder {
        val binding = ItemAddedwordBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WordViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WordViewHolder, position: Int) {
        val wordItem = words[position]
        holder.binding.apply {
            wordAdded.text = wordItem.word
            meaning.text = wordItem.meaning
            pronunciation.setOnClickListener{
                val audioUrl = wordItem.audioUrl
                if (!audioUrl.isNullOrEmpty()) {
                    playAudio(audioUrl)
                } else {
                    Toast.makeText(holder.itemView.context, "No audio available", Toast.LENGTH_SHORT).show()
                }
            }

            root.setOnClickListener {
                onItemClick(wordItem)
            }
        }
    }

    override fun getItemCount() = words.size

    private fun playAudio(audioUrl: String) {
        mediaPlayer?.release()

        try {
            mediaPlayer = MediaPlayer().apply {
                setDataSource(audioUrl)
                prepareAsync()
                setOnPreparedListener {
                    it.start()
                }
                setOnCompletionListener {
                    it.release()
                }
                setOnErrorListener { mp, _, _ ->
                    mp.release()
                    true
                }
            }
        } catch (e: Exception) {
            Log.e("MediaPlayer", "Error playing audio from path: $audioUrl, ${e.message}")
        }
    }

    fun releaseMediaPlayer() {
        mediaPlayer?.release()
        mediaPlayer = null
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newWords: List<WordData>) {
        words = newWords
        notifyDataSetChanged() // Notify the adapter to refresh the UI
    }
}