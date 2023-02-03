package com.base.mvi_arch

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
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


fun Context.toast(message: String, length: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, length).show()
}


inline fun <reified T> FragmentActivity.startActivity(bundle: Bundle? = null) {
    val intent = Intent(this, T::class.java)
    if (bundle != null) {
        intent.putExtras(bundle)
    }
    startActivity(intent)
}


inline fun <reified T> Fragment.startActivity(bundle: Bundle? = null) {
    val intent = Intent(requireContext(), T::class.java)
    if (bundle != null) {
        intent.putExtras(bundle)
    }
    startActivity(intent)
}

