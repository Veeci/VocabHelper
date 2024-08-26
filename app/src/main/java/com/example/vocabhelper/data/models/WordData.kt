package com.example.vocabhelper.data.models

data class WordData(
    val word: String = "",
    val meaning: String? = null,
    val audioUrl: String? = null,
    val category: String? = null,
    val synonym: String? = null,
    val antonym: String? = null,
    val collocation: String? = null,
    val example: String? = null
)
