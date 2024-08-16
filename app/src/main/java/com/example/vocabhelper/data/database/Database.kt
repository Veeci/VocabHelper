package com.example.vocabhelper.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [WordEntity::class], version = 1, exportSchema = false)
@TypeConverters(PhoneticsConverter::class, MeaningsConverter::class, LicenseConverter::class, StringListConverter::class)
abstract class WordDatabase: RoomDatabase() {
    abstract fun wordDao(): WordDAO

    companion object{
        @Volatile
        private var INSTANCE: WordDatabase? = null

        fun getDatabase(context: Context): WordDatabase{
            return INSTANCE ?: synchronized(this){
                val instance = androidx.room.Room.databaseBuilder(
                    context.applicationContext,
                    WordDatabase::class.java,
                    "word_database"
                    ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}