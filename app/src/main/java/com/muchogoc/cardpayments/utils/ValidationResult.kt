package com.muchogoc.cardpayments.utils

data class ValidationResult(
    val isSuccessful: Boolean,
    val errorMessage: String? = null,
)
