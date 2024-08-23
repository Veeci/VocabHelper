package com.example.vocabhelper.data.models

data class WordData(
    val word: String = "",
    val meaning: String? = null,
    val audioUrl: String? = null,
    val category: String? = null,
    val synonyms: List<String>? = null,
    val antonyms: List<String>? = null,
    val collocation: String? = null,
    val example: String? = null
)
