package com.muchogoc.cardpayments.domain.dto

import kotlinx.serialization.Serializable

@Serializable
data class Amount(
    val currency_code: String,
    val value: String
)