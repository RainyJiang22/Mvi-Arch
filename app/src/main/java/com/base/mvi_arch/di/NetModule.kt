package com.base.mvi_arch.di

import com.base.mvi_arch.BuildConfig
import com.base.mvi_arch.network.TravelApi
import com.base.mvi_arch.network.VideoApi
import com.orhanobut.logger.Logger
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * @author jiangshiyu
 * @date 2023/2/2
 */

@Module
@InstallIn(SingletonComponent::class)
class NetModule {

    @Provides
    fun providerOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder().apply {
            if (BuildConfig.DEBUG) {
                addInterceptor(HttpLoggingInterceptor {
                    Logger.d(it)
                }.setLevel(HttpLoggingInterceptor.Level.BODY))
            }
        }.build()
    }


    @Provides
    fun providerRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    fun providerTravelService(retrofit: Retrofit): TravelApi {
        return retrofit.create(TravelApi::class.java)
    }

    @Provides
    fun providerVideoService(retrofit: Retrofit): VideoApi {
        return retrofit.create(VideoApi::class.java)
    }
}