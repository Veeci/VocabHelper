package com.example.vocabhelper.data.models

import com.google.firebase.firestore.PropertyName

data class WordData(
    @PropertyName("word") val word: String = "",
    @PropertyName("meaning") val meaning: String? = null,
    @PropertyName("audioUrl") val audioUrl: String? = null,
    @PropertyName("category") val category: String? = null,
    @PropertyName("synonym") val synonym: String? = null,
    @PropertyName("antonym") val antonym: String? = null,
    @PropertyName("collocation") val collocation: String? = null,
    @PropertyName("example") val example: String? = null
)
