package com.example.hw51.data.model


import com.google.gson.annotations.SerializedName

data class BaseResponse(
    @SerializedName("info")
    val info: Info?,
    @SerializedName("results")
    val characters: List<Character>
)