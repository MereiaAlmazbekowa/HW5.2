package com.example.hw51.data.model.episode

import com.google.gson.annotations.SerializedName

data class Episode(
    @SerializedName("id")
    val id: Int? = null,
    @SerializedName("name")
    val name: String? = null,
    @SerializedName("air_date")
    val airDate: String? = null,
    @SerializedName("episode")
    val episode: String? = null,
    @SerializedName("characters")
    val characters: List<String?>? = null,
    @SerializedName("url")
    val url: String? = null,
    @SerializedName("created")
    val created: String? = null
)
