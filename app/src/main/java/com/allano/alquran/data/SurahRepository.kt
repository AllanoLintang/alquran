package com.allano.alquran.data

import android.util.Log
import com.allano.alquran.data.local.AyahDao
import com.allano.alquran.data.local.AyahEntity
import com.allano.alquran.data.local.SurahDao
import com.allano.alquran.data.local.SurahEntity
import com.allano.alquran.data.model.MergedAyah
import com.allano.alquran.data.model.Surah
import com.allano.alquran.data.network.ApiClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext

class SurahRepository(private val surahDao: SurahDao, private val ayahDao: AyahDao) {

    fun getSurahsStream(): Flow<List<SurahEntity>> = surahDao.getAllSurahs()

    fun getFavoriteSurahsStream(): Flow<List<SurahEntity>> = surahDao.getFavoriteSurahs()

    suspend fun refreshSurahs() {
        withContext(Dispatchers.IO) {
            try {
                val favoriteSurahNumbers = surahDao.getAllSurahs().first().filter { it.isFavorite }.map { it.number }.toSet()
                Log.d("SurahRepository", "Found ${favoriteSurahNumbers.size} favorites to preserve.")

                val response = ApiClient.apiService.getAllSurahs()

                if (response.isSuccessful) {
                    val surahsFromApi = response.body()?.data ?: emptyList()
                    Log.d("SurahRepository", "Sukses mengambil ${surahsFromApi.size} surah dari API")
                    if (surahsFromApi.isNotEmpty()) {
                        val surahEntities = surahsFromApi.map {
                            it.toEntity(isFavorite = favoriteSurahNumbers.contains(it.number))
                        }
                        surahDao.insertAll(surahEntities)
                        Log.d("SurahRepository", "Data berhasil disimpan ke Room with favorite states preserved.")
                    }
                } else {
                    Log.e("SurahRepository", "API Error: ${response.code()} ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e("SurahRepository", "Network or DB Error in refreshSurahs: ${e.message}", e)
            }
        }
    }

    suspend fun setFavorite(surah: SurahEntity, isFavorite: Boolean) {
        surahDao.update(surah.copy(isFavorite = isFavorite))
    }

    private fun Surah.toEntity(isFavorite: Boolean = false): SurahEntity {
        return SurahEntity(
            number = this.number,
            name = this.name,
            englishName = this.englishName,
            englishNameTranslation = this.englishNameTranslation,
            revelationType = this.revelationType,
            numberOfAyahs = this.numberOfAyahs,
            isFavorite = isFavorite
        )
    }

    fun getAyahStream(surahNumber: Int): Flow<List<AyahEntity>> {
        return ayahDao.getAyahsBySurahNumber(surahNumber)
    }
    suspend fun refreshSurahDetail(surahNumber: Int) {
        try {
            val response = ApiClient.apiService.getSurahDetailWithMultipleEditions(surahNumber)
            if (response.isSuccessful) {
                val responseData = response.body()?.data
                val arabicEdition = responseData?.getOrNull(0)
                val englishEdition = responseData?.getOrNull(1)

                if (arabicEdition != null && englishEdition != null) {
                    val mergedAyahs = arabicEdition.ayahs.zip(englishEdition.ayahs).map { (arabicAyah, englishAyah) ->
                        AyahEntity(
                            number = arabicAyah.number,
                            surahNumber = surahNumber,
                            numberInSurah = arabicAyah.numberInSurah,
                            arabicText = arabicAyah.text,
                            englishText = englishAyah.text
                        )
                    }
                    ayahDao.insertAll(mergedAyahs)
                    Log.d("SurahRepository", "Detail ayat untuk surah $surahNumber disimpan ke DB.")
                }
            }
        } catch (e: Exception) {
            Log.e("SurahRepository", "Gagal me-refresh detail surah: ", e)
        }
    }
    suspend fun getSurahDetailFromApi(surahNumber: Int): List<MergedAyah>? {
        return try {
            val response = ApiClient.apiService.getSurahDetailWithMultipleEditions(surahNumber)

            if (response.isSuccessful) {
                val responseData = response.body()?.data

                val arabicEdition = responseData?.getOrNull(0)
                val englishEdition = responseData?.getOrNull(1)

                if (arabicEdition != null && englishEdition != null) {
                    val mergedList = arabicEdition.ayahs.zip(englishEdition.ayahs).map { (arabicAyah, englishAyah) ->
                        MergedAyah(
                            numberInSurah = arabicAyah.numberInSurah,
                            arabicText = arabicAyah.text,
                            englishText = englishAyah.text
                        )
                    }
                    return mergedList
                } else {
                    return null
                }
            } else {
                return null
            }
        } catch (e: Exception) {
            return null
        }
    }
}
