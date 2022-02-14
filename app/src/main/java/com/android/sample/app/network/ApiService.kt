package com.android.sample.app.network

import com.android.sample.app.domain.Dashboard
import com.android.sample.app.domain.Section
import retrofit2.http.GET
import retrofit2.http.Url

interface ApiService {

    @GET("androidexo-se")
    suspend fun getDashboard(): Dashboard

    @GET
    suspend fun getSection(@Url url: String): Section
}