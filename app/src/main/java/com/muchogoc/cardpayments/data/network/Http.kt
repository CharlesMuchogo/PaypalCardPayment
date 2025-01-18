package com.muchogoc.cardpayments.data.network

import com.muchogoc.cardpayments.data.network.UrlBuilder.httpUrlBuilder
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

object Http {
    val client by lazy {
        HttpClient(OkHttp.create {}) {
            install(Logging) {
                logger =
                    object : Logger {
                        override fun log(message: String) {
                            println(message)
                        }
                    }
                level = LogLevel.ALL
            }

            install(HttpTimeout) {
                requestTimeoutMillis = 180_000L
                socketTimeoutMillis = 180_000L
                connectTimeoutMillis = 180_000L
            }

            install(ContentNegotiation) {
                json(
                    Json {
                        prettyPrint = true
                        isLenient = true
                        ignoreUnknownKeys = true
                    },
                )
            }

            defaultRequest {
                url(httpUrlBuilder())

            }
        }
    }
}
