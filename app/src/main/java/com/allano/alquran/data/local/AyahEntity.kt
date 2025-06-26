package com.allano.alquran.data.local

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "ayahs",
    foreignKeys = [
        ForeignKey(
            entity = SurahEntity::class,
            parentColumns = ["number"],
            childColumns = ["surahNumber"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["surahNumber"])]
)
data class AyahEntity(
    @PrimaryKey
    val number: Int,
    val surahNumber: Int,
    val numberInSurah: Int,
    val arabicText: String,
    val englishText: String
)