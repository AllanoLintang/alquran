@file:OptIn(InternalSerializationApi::class)

package com.allano.alquran.data.model

import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.OptIn


@Serializable
data class Surah(
    val number: Int,
    val name: String,
    @SerialName("englishName")
    val englishName: String,
    @SerialName("englishNameTranslation")
    val englishNameTranslation: String,
    @SerialName("revelationType")
    val revelationType: String,
    val numberOfAyahs: Int
)

@Serializable
data class SurahResponse(
    val code: Int,
    val status: String,
    val data: List<Surah>
)

sealed class ApiResponse<out T> {
    data class Success<out T>(val data: T) : ApiResponse<T>()
    data class Error(val errorMessage: String) : ApiResponse<Nothing>()
}