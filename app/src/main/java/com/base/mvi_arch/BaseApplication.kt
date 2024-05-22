package com.base.mvi_arch

import android.app.Application
import com.biubiu.eventbus.EventBusInitializer
import dagger.hilt.android.HiltAndroidApp

/**
 * @author jiangshiyu
 * @date 2023/2/2
 */

@HiltAndroidApp
class BaseApplication : Application() {


    override fun onCreate() {
        super.onCreate()
        EventBusInitializer.init(this)
    }
}