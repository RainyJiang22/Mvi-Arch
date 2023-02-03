package com.base.mvi_arch.startup

import android.content.Context
import androidx.startup.Initializer
import com.base.mvi_arch.global.Configurator
import com.base.mvi_arch.network.URLS
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger

/**
 * @author jiangshiyu
 * @date 2023/2/3
 */
class AppInitializer : Initializer<Unit> {
    override fun create(context: Context) {
        Configurator.withApiHost(URLS.VIDEO_LIST_URL)
            .withApplicationContext(context.applicationContext)
            .configure()
        Logger.addLogAdapter(AndroidLogAdapter())
    }

    override fun dependencies(): MutableList<Class<out Initializer<*>>> {
        return mutableListOf()
    }
}