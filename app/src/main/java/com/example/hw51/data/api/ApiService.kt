package com.example.hw51.data.api

import com.example.hw51.data.model.BaseResponse
import retrofit2.http.GET

interface ApiService {

    @GET("character")
    suspend fun getCharacters(): BaseResponse
}