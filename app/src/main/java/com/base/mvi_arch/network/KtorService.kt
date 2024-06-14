package com.base.mvi_arch.network

import arrow.core.Either
import com.base.mvi_arch.data.Articles
import io.ktor.client.request.get

/**
 * @author jiangshiyu
 * @date 2024/6/13
 */

val ktorSingleton = KtorClient.ktorClient()

suspend fun fetchTestAPI(): Either<HttpError, List<Articles>> =
    httpRequest(ktorSingleton) {
        get("https://jsonplaceholder.typicode.com/posts")
    }
