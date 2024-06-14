package com.base.mvi_arch.network

import android.util.Log
import arrow.core.Either
import com.base.mvi_arch.data.Post
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json

/**
 * @author jiangshiyu
 * @date 2024/5/21
 */
object KtorClient {

    val httpClient = HttpClient(Android) {

        install(Logging) {
            level = LogLevel.BODY
        }
    }

    val apiService = ApiService(httpClient)

    fun ktorClient(): HttpClient = HttpClient {
        install(Logging) {
            logger = object : Logger {
                override fun log(message: String) {
                    Log.d("KTOR", message)
                }
            }
            level = LogLevel.ALL // logs everything
        }

        install(HttpTimeout) {
            requestTimeoutMillis = 10_000 // 10s
            connectTimeoutMillis = 10_000 // 10s
        }

        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                isLenient = true
                encodeDefaults = false
            })
        }
    }
}



class ApiService(private val client: HttpClient) {

    suspend fun getArticles(): String {
        val response: HttpResponse = client.get("https://jsonplaceholder.typicode.com/posts")
        return response.bodyAsText()
    }

    suspend fun createPost(post: Post): HttpResponse {
        return client.post {
            url("https://httpbin.org/post")
            contentType(ContentType.Application.Json)
            setBody(post)
        }
    }
}


sealed interface HttpError {
    data class API(val response: HttpResponse) : HttpError
    data class Unknown(val exception: Exception) : HttpError
}

suspend inline fun <reified Data> httpRequest(
    ktorClient: HttpClient,
    crossinline request: suspend HttpClient.() -> HttpResponse
): Either<HttpError, Data> = withContext(Dispatchers.IO) {
    try {
        val response = request(ktorClient)
        if (response.status.isSuccess()) {
            // Success: 200 <=status code <=299.
            Either.Right(response.body())
        } else {
            // Failure: unsuccessful status code.
            Either.Left(HttpError.API(response))
        }
    } catch (exception: Exception) {
        // Failure: exceptional, something wrong.
        Either.Left(HttpError.Unknown(exception))
    }
}