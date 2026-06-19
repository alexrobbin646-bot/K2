package com.example

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Dorama(
    val id: String,
    val title: String,
    val imageUrl: String,
    val description: String,
    val videoUrl: String
)

@JsonClass(generateAdapter = true)
data class DoramaResponse(
    val doramas: List<Dorama>
)
