package com.muchogoc.cardpayments.data.network

import com.muchogoc.blogapp.presentation.utils.Results
import io.ktor.http.HttpStatusCode

object ApiHelper {
    suspend fun <T : Any> safeApiCall(
        statusCode: HttpStatusCode,
        apiCall: suspend () -> T,
    ): Results<T> =
        try {
            when (statusCode) {
                HttpStatusCode.Unauthorized -> {
                    Results.error("Your session has expired. Login again to continue")
                }

                else -> {
                    val result = apiCall.invoke()
                    Results.success(result)
                }
            }
        } catch (throwable: Throwable) {
            throwable.printStackTrace()
            Results.error("Server error ${throwable.message}")
        }
}
