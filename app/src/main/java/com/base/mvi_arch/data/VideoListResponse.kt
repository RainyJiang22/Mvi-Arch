package com.base.mvi_arch.data

import com.google.gson.annotations.SerializedName

/**
 * @author jiangshiyu
 * @date 2023/2/2
 */
data class VideoListResponse(
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: VideoListModel,
)

data class VideoListModel(
    @SerializedName("total") val total: Int,
    @SerializedName("list") val list: List<VideoModel>,
)

data class VideoModel(
    @SerializedName("title") val title: String,
    @SerializedName("userName") val userName: String,
    @SerializedName("userPic") val userPic: String,
    @SerializedName("coverUrl") val coverUrl: String,
    @SerializedName("playUrl") val playUrl: String,
    @SerializedName("duration") val duration: String,
)