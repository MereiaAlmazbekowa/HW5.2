package com.example.hw51.data.api
import com.example.hw51.data.model.BaseResponse
import com.example.hw51.data.model.Character
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {

    @GET("character")
    fun getCharacters(): Call<BaseResponse>

    @GET("character/{id}")
    fun getCharacterById(@Path("id") id: Int): Call<Character>
}