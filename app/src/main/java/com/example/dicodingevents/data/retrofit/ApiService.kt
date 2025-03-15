package com.example.dicodingevents.data.retrofit

import com.example.dicodingevents.data.response.DetailResponse
import com.example.dicodingevents.data.response.DicodingEventsResponse
import com.example.dicodingevents.data.response.ListEventsItem
import okhttp3.Call
import retrofit2.Callback
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("events")
    fun getEvent(
        @Query("active") number: Int
    ) : retrofit2.Call<DicodingEventsResponse>

    @GET("events/{id}")
    fun getDetailEvent(
        @Path("id") id: String
        ) : retrofit2.Call<DetailResponse>
}