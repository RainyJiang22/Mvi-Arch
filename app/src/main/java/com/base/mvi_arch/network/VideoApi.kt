package com.base.mvi_arch.network

import com.base.mvi_arch.data.VideoListModel
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

/**
 * @author jiangshiyu
 * @date 2023/2/2
 */
interface VideoApi {

    @GET
    suspend fun getVideoList(
        @Url url: String = URLS.VIDEO_LIST_URL,
        @Query("page") page: Int,
        @Query("size") size: Int = 10,
    ): VideoListModel
}