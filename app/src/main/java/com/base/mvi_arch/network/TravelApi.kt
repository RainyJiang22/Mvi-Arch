package com.base.mvi_arch.network

import com.base.mvi_arch.data.Params
import com.base.mvi_arch.data.TravelResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Url

/**
 * @author jiangshiyu
 * @date 2023/2/2
 */
interface TravelApi {

    @GET
    suspend fun getTravelTab(@Url url: String = URLS.TRAVEL_TAB_URL): TravelResponse

    @POST
    suspend fun getTravelCategoryList(@Url url: String, @Body params: Params): TravelResponse
}