package com.allano.alquran.data.network

import com.allano.alquran.data.model.SurahResponse
import com.allano.alquran.data.model.MultiEditionSurahDetailResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path


interface ApiService {
    @GET("v1/surah")
    suspend fun getAllSurahs(): Response<SurahResponse>

    @GET("v1/surah/{number}/editions/quran-uthmani,en.asad")
    suspend fun getSurahDetailWithMultipleEditions(
        @Path("number") surahNumber: Int
    ): Response<MultiEditionSurahDetailResponse>

}