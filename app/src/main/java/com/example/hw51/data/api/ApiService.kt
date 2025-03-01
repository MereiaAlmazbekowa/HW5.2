package com.example.hw51.data.api
import com.example.hw51.data.model.BaseResponse
import com.example.hw51.data.model.Character
import com.example.hw51.data.model.episode.Episode
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url

interface ApiService {

    @GET("character")
    fun getCharacters(): Call<BaseResponse>

    @GET("character/{id}")
    fun getCharacterById(@Path("id") id: Int): Call<Character>

    @GET("character")
    suspend fun getCharactersPaging(
        @Query("page") page: Int
    ): Response<BaseResponse>

    @GET
    fun getEpisodeName(@Url url: String): Call<Episode>
}