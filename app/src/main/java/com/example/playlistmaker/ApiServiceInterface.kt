package com.example.playlistmaker

import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiServiceInterface {
        @GET("/search")
        fun searchQuery(@Query("term") term: String, @Query("entity") entity: String): Call<JsonObject>
    }