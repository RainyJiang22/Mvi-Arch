package com.base.mvi_arch

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

/**
 * @author jiangshiyu
 * @date 2022/6/16
 */

const val BASE_URL = "https://wanandroid.com"


//state -> Loading/Success/Failed
sealed class PageState<out T> {
    data class Success<T>(val data: T) : PageState<T>()

    data class Fail<T>(val message: String) : PageState<T>() {
        constructor(t: Throwable) : this(t.message ?: "")
    }
}

fun <T> MutableLiveData<T>.asLiveData(): LiveData<T> {
    return this
}




