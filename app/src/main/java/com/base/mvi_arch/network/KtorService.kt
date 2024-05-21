package com.base.mvi_arch.network

import android.util.Log
import com.base.mvi_arch.data.Params
import com.base.mvi_arch.data.TravelResponse
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.utils.EmptyContent.contentType
import io.ktor.http.ContentType

/**
 * @author jiangshiyu
 * @date 2024/5/21
 */
object KtorService {

    val client: HttpClient
        get() = HttpClient(Android) {
            engine {
                // this: AndroidEngineConfig
                connectTimeout = 100_000
                socketTimeout = 100_000
            }
        }

    suspend fun getTravelTab() {
        kotlin.runCatching {
            client.get(URLS.TRAVEL_TAB_URL) {
                print(this.body)
                Log.d("travel", "getTravelTab: ${this.body}")
            }
        }
    }

    suspend fun getTavelCategoryList(url: String, params: Params) {
        kotlin.runCatching {
            client.post(url) {
                setBody(params)
            }
        }
    }
}