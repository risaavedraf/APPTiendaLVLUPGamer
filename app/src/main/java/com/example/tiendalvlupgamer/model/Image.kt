package com.example.tiendalvlupgamer.model

import com.google.gson.annotations.SerializedName

data class ImageResponse(
    @SerializedName("data") val data: String,
    @SerializedName("contentType") val contentType: String,
    @SerializedName("filename") val filename: String
)
