package com.muchogoc.cardpayments.data.remote

import com.muchogoc.blogapp.presentation.utils.Results
import com.muchogoc.cardpayments.domain.dto.GetOrderIdResponseDTO
import com.muchogoc.cardpayments.domain.dto.GetAccessTokenResponseDTO
import com.muchogoc.cardpayments.domain.dto.OrderIdRequest

interface RemoteRepository {
   suspend fun getOrderId(request: OrderIdRequest, accessToken: String) : Results<GetOrderIdResponseDTO>
   suspend fun getAccessToken() : Results<GetAccessTokenResponseDTO>
}