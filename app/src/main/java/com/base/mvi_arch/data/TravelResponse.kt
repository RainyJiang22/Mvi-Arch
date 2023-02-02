package com.base.mvi_arch.data

import com.google.gson.annotations.SerializedName

/**
 * @author jiangshiyu
 * @date 2023/2/2
 */
data class TravelResponse(
    @SerializedName("url")
    val url: String,

    @SerializedName("params")
    val params: Params,

    @SerializedName("tabs")
    val tabs: List<Tab>
)

data class Tab(
    @SerializedName("labelName")
    val labelName: String,

    @SerializedName("groupChannelCide")
    val groupChannelCode: String,

    @SerializedName("type")
    val type: Int,
)

data class Params(
    @SerializedName("districtId")
    val districtId: Int,
    @SerializedName("groupChannelCode")
    var groupChannelCode: String,
    @SerializedName("type")
    var type: Any,
    @SerializedName("lat")
    val lat: Double,
    @SerializedName("lon")
    val lon: Double,
    @SerializedName("contentType")
    val contentType: String,
    @SerializedName("head")
    val head: Head,
    @SerializedName("imageCutType")
    val imageCutType: Int,
    @SerializedName("locatedDistrictId")
    val locatedDistrictId: Int,
    @SerializedName("pagePara")
    val pagePara: PagePara,
)

data class Head(

    @SerializedName("cid")
    val cid: String,

    @SerializedName("ctok")
    val ctok: String,

    @SerializedName("cver")
    val cver: String,

    @SerializedName("lang")
    val lang: String,

    @SerializedName("sid")
    val sid: String,

    @SerializedName("syscode")
    val syscode: String,

    @SerializedName("auth")
    val auth: Any,

    @SerializedName("extension")
    val extension: List<Extension>,
)

data class PagePara(
    @SerializedName("pageIndex")
    val pageIndex: Int,

    @SerializedName("pageSize")
    val pageSize: Int,

    @SerializedName("sortType")
    val sortType: Int,

    @SerializedName("sortDirection")
    val sortDirection: Int,
)

data class Extension(
    @SerializedName("name")
    val name: String,

    @SerializedName("value")
    val value: String,
)
