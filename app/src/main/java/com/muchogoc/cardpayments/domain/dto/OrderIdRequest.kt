package com.muchogoc.cardpayments.domain.dto

import kotlinx.serialization.Serializable

@Serializable
data class OrderIdRequest(
    val intent: String,
    val purchase_units: List<PurchaseUnit>
)