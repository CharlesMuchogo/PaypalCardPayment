package com.muchogoc.cardpayments.domain.dto

import kotlinx.serialization.Serializable

@Serializable
data class ErrorDTO(
    val message: String,
)
