package com.muchogoc.cardpayments.domain.dto

import kotlinx.serialization.Serializable

@Serializable
data class GetAccessTokenResponseDTO(
    val access_token: String,
    val app_id: String,
    val expires_in: Int,
    val nonce: String,
    val scope: String,
    val token_type: String
)