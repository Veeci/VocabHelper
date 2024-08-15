package com.example.vocabhelper.data.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Response(
	@SerializedName("word") val word: String? = null,
	@SerializedName("phonetic") val phonetic: String? = null,
	@SerializedName("phonetics") val phonetics: List<PhoneticsItem?>? = null,
	@SerializedName("meanings") val meanings: List<MeaningsItem?>? = null,
	@SerializedName("license") val license: License? = null,
	@SerializedName("sourceUrls") val sourceUrls: List<String?>? = null
) : Parcelable

@Parcelize
data class PhoneticsItem(
	@SerializedName("text") val text: String? = null,
	@SerializedName("audio") val audio: String? = null,
	@SerializedName("sourceUrl") val sourceUrl: String? = null,
	@SerializedName("license") val license: License? = null
) : Parcelable

@Parcelize
data class MeaningsItem(
	@SerializedName("partOfSpeech") val partOfSpeech: String? = null,
	@SerializedName("definitions") val definitions: List<DefinitionsItem?>? = null,
	@SerializedName("synonyms") val synonyms: List<String>? = null,
	@SerializedName("antonyms") val antonyms: List<String>? = null
) : Parcelable

@Parcelize
data class DefinitionsItem(
	@SerializedName("definition") val definition: String? = null,
	@SerializedName("example") val example: String? = null,
	@SerializedName("synonyms") val synonyms: List<String>? = null,
	@SerializedName("antonyms") val antonyms: List<String>? = null
) : Parcelable

@Parcelize
data class License(
	@SerializedName("name") val name: String? = null,
	@SerializedName("url") val url: String? = null
) : Parcelable