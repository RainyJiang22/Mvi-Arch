package com.base.mvi_arch.network

import com.base.mvi_arch.BASE_URL
import com.base.mvi_arch.data.ApiResponse
import com.base.mvi_arch.data.UserInfoResponse
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

/**
 * @author jiangshiyu
 * @date 2022/6/16
 */
interface WanApi {


    companion object {
        fun create(): WanApi {
            val okHttpClient = OkHttpClient()
                .newBuilder()
                .build()

            return Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(WanApi::class.java)
        }
    }


    //登录
    @POST("/user/login")
    @FormUrlEncoded
    suspend fun login(
        @Field("username") username: String,
        @Field("password") password: String
    ): ApiResponse<UserInfoResponse>

    //注册
    @POST("/user/register")
    @FormUrlEncoded
    suspend fun register(
        @Field("username") username: String,
        @Field("password") password: String,
        @Field("repassword") repassowrd: String
    ): ApiResponse<UserInfoResponse>

}