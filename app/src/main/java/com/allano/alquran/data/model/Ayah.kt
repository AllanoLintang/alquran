@file:OptIn(InternalSerializationApi::class)

package com.allano.alquran.data.model

import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.Serializable

@Serializable
data class Ayah(
    val number: Int,
    val numberInSurah: Int,
    val text: String,
    val juz: Int
)

@Serializable
data class SurahDetail(
    val number: Int,
    val name: String,
    val englishName: String,
    val englishNameTranslation: String,
    val revelationType: String,
    val ayahs: List<Ayah>
)

@Serializable
data class MultiEditionSurahDetailResponse(
    val code: Int,
    val status: String,
    val data: List<SurahDetail>
)

data class MergedAyah(
    val numberInSurah: Int,
    val arabicText: String,
    val englishText: String
)