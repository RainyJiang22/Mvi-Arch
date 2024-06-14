package com.base.mvi_arch.data

import com.google.gson.annotations.SerializedName


data class Articles(
    @SerializedName("body")
    val body: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("title")
    val title: String,
    @SerializedName("userId")
    val userId: Int
)