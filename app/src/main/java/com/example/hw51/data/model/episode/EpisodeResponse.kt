package com.example.hw51.data.model.episode

import com.google.gson.annotations.SerializedName

data class EpisodeResponse(
    @SerializedName("info")
    val info: Info? = null,
    @SerializedName("results")
    val episodes: List<Episode>? = null
)