package com.muchogoc.cardpayments.domain.dto

import kotlinx.serialization.Serializable

@Serializable
data class GetOrderIdResponseDTO(
    val id: String,
    val status: String
)