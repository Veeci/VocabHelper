package com.example.vocabhelper.data.database

import androidx.room.TypeConverter
import com.example.vocabhelper.data.models.License
import com.example.vocabhelper.data.models.MeaningsItem
import com.example.vocabhelper.data.models.PhoneticsItem
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class PhoneticsConverter {
    @TypeConverter
    fun fromPhoneticsList(phonetics: List<PhoneticsItem>?): String? {
        return Gson().toJson(phonetics)
    }

    @TypeConverter
    fun toPhoneticsList(phoneticsString: String?): List<PhoneticsItem>? {
        return if (phoneticsString == null) null else Gson().fromJson(phoneticsString, object : TypeToken<List<PhoneticsItem>>() {}.type)
    }
}

class MeaningsConverter {
    @TypeConverter
    fun fromMeaningsList(meanings: List<MeaningsItem>?): String? {
        return Gson().toJson(meanings)
    }

    @TypeConverter
    fun toMeaningsList(meaningsString: String?): List<MeaningsItem>? {
        return if (meaningsString == null) null else Gson().fromJson(meaningsString, object : TypeToken<List<MeaningsItem>>() {}.type)
    }
}

class LicenseConverter {
    @TypeConverter
    fun fromLicense(license: License?): String? {
        return Gson().toJson(license)
    }

    @TypeConverter
    fun toLicense(licenseString: String?): License? {
        return if (licenseString == null) null else Gson().fromJson(licenseString, License::class.java)
    }
}

class StringListConverter {
    @TypeConverter
    fun fromStringList(strings: List<String>?): String? {
        return Gson().toJson(strings)
    }

    @TypeConverter
    fun toStringList(stringsString: String?): List<String>? {
        return if (stringsString == null) null else Gson().fromJson(stringsString, object : TypeToken<List<String>>() {}.type)
    }
}