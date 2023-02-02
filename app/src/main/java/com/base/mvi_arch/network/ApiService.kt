package com.base.mvi_arch.network

import com.base.mvi_arch.BuildConfig
import com.base.mvi_arch.global.ConfigKeys
import com.base.mvi_arch.global.Configurator
import com.orhanobut.logger.Logger
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * @author jiangshiyu
 * @date 2023/2/2
 */

private val retrofit by lazy {
    Retrofit.Builder()
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(Configurator.getConfiguration<String>(ConfigKeys.API_HOST))
        .build()
}

private val okHttpClient by lazy {
    OkHttpClient.Builder()
        .apply {
            if (BuildConfig.DEBUG) {
                addInterceptor(HttpLoggingInterceptor {
                    Logger.d(it)
                }.setLevel(HttpLoggingInterceptor.Level.BODY))
            }
        }.build()
}

object TravelApiService : TravelApi by retrofit.create(TravelApi::class.java)

object VideoApiService : VideoApi by retrofit.create(VideoApi::class.java)