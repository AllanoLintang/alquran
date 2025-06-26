package com.allano.alquran.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface SurahDao {
    @Query("SELECT * FROM surahs")
    fun getAllSurahs(): Flow<List<SurahEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(surahs: List<SurahEntity>)

    @Query("DELETE FROM surahs")
    suspend fun deleteAll()

    @Update
    suspend fun update(surah: SurahEntity)

    @Query("SELECT * FROM surahs WHERE isFavorite = 1")
    fun getFavoriteSurahs(): Flow<List<SurahEntity>>
}