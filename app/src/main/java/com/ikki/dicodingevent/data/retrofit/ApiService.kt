package com.ikki.dicodingevent.data.retrofit

import com.ikki.dicodingevent.data.response.DetailEventResponse
import com.ikki.dicodingevent.data.response.Response
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface ApiService {
    @GET("events?active=1")
    suspend fun getUpcomingEvents(): Response

    @GET("events")
    suspend fun getAllEvents(): Response

    @GET("events/{id}")
    fun getDetail(@Path("id") id : Int) : Call<DetailEventResponse>

    companion object {
        private const val BASE_URL = "https://event-api.dicoding.dev/"

        fun create(): ApiService {
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            return retrofit.create(ApiService::class.java)
        }
    }
}