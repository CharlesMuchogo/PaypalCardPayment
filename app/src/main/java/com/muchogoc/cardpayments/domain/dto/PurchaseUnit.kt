package com.muchogoc.cardpayments.domain.dto

import kotlinx.serialization.Serializable

@Serializable
data class PurchaseUnit(
    val amount: Amount
)