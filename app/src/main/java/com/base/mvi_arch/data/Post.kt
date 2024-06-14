package com.base.mvi_arch.data

import kotlinx.serialization.Serializable

@Serializable
data class Post(
    val userId: Int,
    val title: String,
    val body: String
)

@Serializable
data class PostResponse(
    val json: Post
)