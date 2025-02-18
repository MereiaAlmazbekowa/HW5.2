package com.example.hw51.data.api

import com.example.hw51.data.model.BaseResponse
import com.example.hw51.data.model.Character
import retrofit2.Call
import retrofit2.http.GET

interface ApiService {

    @GET("character")
    fun getCharacters(): BaseResponse
}