package com.base.mvi_arch.di

import com.base.mvi_arch.global.gson
import com.google.gson.Gson
import dagger.Module
import dagger.hilt.InstallIn

/**
 * @author jiangshiyu
 * @date 2023/2/2
 */

@Module
@InstallIn(SingleTonModule::class)
class SingleTonModule {

    fun providerGson(): Gson {
        return Gson()
    }
}