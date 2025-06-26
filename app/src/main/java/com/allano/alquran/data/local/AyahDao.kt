package com.allano.alquran.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface AyahDao {

    @Query("SELECT * FROM ayahs WHERE surahNumber = :surahNumber ORDER BY numberInSurah ASC")
    fun getAyahsBySurahNumber(surahNumber: Int): Flow<List<AyahEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(ayahs: List<AyahEntity>)

}