package com.example.vocabhelper.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface WordDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWord(word: WordEntity)

    @Query("SELECT * FROM word_table WHERE word = :word")
    suspend fun getWord(word: String): WordEntity?

    @Query("DELETE FROM word_table WHERE word = :word")
    suspend fun deleteWord(word: String)

    @Query("SELECT * FROM word_table")
    suspend fun getAllWords(): List<WordEntity>

    @Query("SELECT * FROM word_table WHERE category = :category")
    suspend fun getWordsByCategory(category: String): List<WordEntity>
}