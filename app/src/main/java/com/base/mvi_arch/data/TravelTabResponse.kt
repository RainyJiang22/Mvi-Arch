package com.base.mvi_arch.data

import com.google.gson.annotations.SerializedName

/**
 * @author jiangshiyu
 * @date 2023/2/2
 */


data class TravelTabResponse(
    @SerializedName("totalCount")
    val totalCount: Int,
    @SerializedName("resultList")
    val resultList: List<TravelItem>,
)

data class TravelItem(
    @SerializedName("type")
    val type: Int,
    @SerializedName("article")
    val article: Article,
)

data class Article(
    @SerializedName("articleId")
    val articleId: Int,

    @SerializedName("productType")
    val productType: Int,
    @SerializedName("sourceType")
    val sourceType: Int,
    @SerializedName("articleTitle")
    val articleTitle: String,
    @SerializedName("author")
    val author: Author,
    @SerializedName("images")
    val images: List<Images>,
    @SerializedName("hasVideo")
    val hasVideo: Boolean,
    @SerializedName("readCount")
    val readCount: Int,
    @SerializedName("likeCount")
    val likeCount: Int,
    @SerializedName("commentCount")
    val commentCount: Int,
    @SerializedName("urls")
    val urls: List<Urls>,
    @SerializedName("tags")
    val tags: List<Tags>,
    @SerializedName("topics")
    val topics: List<Topics>,
    @SerializedName("pois")
    val pois: List<Pois>,
    @SerializedName("publishTime")
    val publishTime: String,
    @SerializedName("publishTimeDisplay")
    val publishTimeDisplay: String,
    @SerializedName("shootTime")
    val shootTime: String,
    @SerializedName("shootTimeDisplay")
    val shootTimeDisplay: String,
    @SerializedName("level")
    val level: Int,
    @SerializedName("distanceText")
    val distanceText: String,
    @SerializedName("isLike")
    val isLike: Boolean,
    @SerializedName("imageCounts")
    val imageCounts: Int,
    @SerializedName("isCollected")
    val isCollected: Boolean,
    @SerializedName("collectCount")
    val collectCount: Int,
    @SerializedName("articleStatus")
    val articleStatus: Int,
    @SerializedName("poiName")
    val poiName: String,
)

data class Author(
    @SerializedName("authorId") val authorId: Int,
    @SerializedName("nickName") val nickName: String,
    @SerializedName("clientAuth") val clientAuth: String,
    @SerializedName("jumpUrl") val jumpUrl: String,
    @SerializedName("coverImage") val coverImage: CoverImage,
)

data class CoverImage(
    @SerializedName("dynamicUrl") val dynamicUrl: String,
    @SerializedName("originalUrl") val originalUrl: String,
)

data class Images(
    @SerializedName("imageId") val imageId: Int,
    @SerializedName("dynamicUrl") val dynamicUrl: String,
    @SerializedName("originalUrl") val originalUrl: String,
    @SerializedName("width") val width: Double,
    @SerializedName("height") val height: Double,
    @SerializedName("mediaType") val mediaType: Int,
    @SerializedName("lat") val lat: Double,
    @SerializedName("lon") val lon: Double,
)

data class Urls(
    @SerializedName("version") val version: String,
    @SerializedName("appUrl") val appUrl: String,
    @SerializedName("h5Url") val h5Url: String?,
    @SerializedName("wxUrl") val wxUrl: String,
)

data class Tags(
    @SerializedName("tagId") val tagId: Int,
    @SerializedName("tagName") val tagName: String,
    @SerializedName("tagLevel") val tagLevel: Int,
    @SerializedName("parentTagId") val parentTagId: Int,
    @SerializedName("source") val source: Int,
    @SerializedName("sortIndex") val sortIndex: Int,
)

data class Topics(
    @SerializedName("topicId") val topicId: Int,
    @SerializedName("topicName") val topicName: String,
    @SerializedName("level") val level: Int,
)

data class Pois(
    @SerializedName("poiType") val poiType: Int,
    @SerializedName("poiId") val poiId: Int,
    @SerializedName("poiName") val poiName: String,
    @SerializedName("districtId") val districtId: Int,
    @SerializedName("districtName") val districtName: String,
    @SerializedName("poiExt") val poiExt: PoiExt,
    @SerializedName("source") val source: Int,
    @SerializedName("isMain") val isMain: Int,
    @SerializedName("isInChin") val isInChin: Boolean,
)

data class PoiExt(
    @SerializedName("h5Url") val h5Url: String,
    @SerializedName("appUrl") val appUrl: String,
)
