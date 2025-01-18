package com.muchogoc.cardpayments.data.remote

import android.annotation.SuppressLint
import com.muchogoc.blogapp.presentation.utils.Results
import com.muchogoc.cardpayments.data.local.PreferenceManager.CLIENT_ID
import com.muchogoc.cardpayments.data.local.PreferenceManager.CLIENT_SECRET
import com.muchogoc.cardpayments.data.network.ApiHelper
import com.muchogoc.cardpayments.data.network.Http
import com.muchogoc.cardpayments.domain.dto.ErrorDTO
import com.muchogoc.cardpayments.domain.dto.GetAccessTokenResponseDTO
import com.muchogoc.cardpayments.domain.dto.OrderIdRequest
import com.muchogoc.cardpayments.domain.dto.GetOrderIdResponseDTO
import com.muchogoc.cardpayments.utils.Utils.decodeExceptionMessage
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import java.util.Base64

class RemoteRepositoryImpl: RemoteRepository {
    override suspend fun getOrderId(request: OrderIdRequest, accessToken: String): Results<GetOrderIdResponseDTO> {
        return try {

            val response = Http.client.post("/v2/checkout/orders/") {
                contentType(ContentType.Application.Json)
                header(HttpHeaders.Authorization, "Bearer $accessToken")
                setBody(request)
            }

            if (response.status != HttpStatusCode.Created) {
                val apiResponse =
                    ApiHelper.safeApiCall(response.status) {
                        response.body<ErrorDTO>()
                    }

                    Results.error(
                        apiResponse.data?.message ?: "Error getting orderId. Try again"
                    )

            } else {
                val apiResponse =
                    ApiHelper.safeApiCall(response.status) {
                        response.body<GetOrderIdResponseDTO>()
                    }
                apiResponse
            }
        } catch (e: Exception) {
            Results.error(decodeExceptionMessage(e))
        }
    }

    @SuppressLint("NewApi")
    override suspend fun getAccessToken(): Results<GetAccessTokenResponseDTO> {
        return try {
            val clientId = CLIENT_ID
            val clientSecret = CLIENT_SECRET
            val credentials = "$clientId:$clientSecret"
            val encodedCredentials =  Base64.getEncoder().encodeToString(credentials.toByteArray())

            val response = Http.client.post("/v1/oauth2/token") {
                header(HttpHeaders.Authorization, "Basic $encodedCredentials")
                header(HttpHeaders.ContentType, ContentType.Application.FormUrlEncoded)
                setBody("grant_type=client_credentials")
            }

            if (response.status != HttpStatusCode.OK) {
                val apiResponse =
                    ApiHelper.safeApiCall(response.status) {
                        response.body<ErrorDTO>()
                    }

                Results.error(
                    apiResponse.data?.message ?: "Error getting token. Try again"
                )
            } else {
                val apiResponse =
                    ApiHelper.safeApiCall(response.status) {
                        response.body<GetAccessTokenResponseDTO>()
                    }
                apiResponse
            }
        } catch (e: Exception) {
            Results.error(decodeExceptionMessage(e))
        }
    }


}