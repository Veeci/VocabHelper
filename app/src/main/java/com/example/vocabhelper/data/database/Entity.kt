package com.example.vocabhelper.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.vocabhelper.data.models.License
import com.example.vocabhelper.data.models.MeaningsItem
import com.example.vocabhelper.data.models.PhoneticsItem

@Entity(tableName = "word_table")
data class WordEntity (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val word: String,
    val phonetic: String?,
    val category: String,
    val collocation: List<String>?,
    val synonym: List<String>?,
    val antonym: List<String>?,
    val example: List<String>?,
    @TypeConverters(PhoneticsConverter::class) val phonetics: List<PhoneticsItem>?,
    @TypeConverters(MeaningsConverter::class) val meanings: List<MeaningsItem>?,
    @TypeConverters(LicenseConverter::class) val license: License?,
    @TypeConverters(StringListConverter::class) val sourceUrls: List<String>?
)