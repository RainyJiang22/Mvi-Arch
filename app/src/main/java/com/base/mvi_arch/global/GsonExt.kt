package com.base.mvi_arch.global

import com.google.gson.Gson

/**
 * @author jiangshiyu
 * @date 2023/2/2
 */

val gson by lazy { Gson() }


inline fun <reified T> fromJson(json: String): T {
    return gson.fromJson(json, T::class.java)
}