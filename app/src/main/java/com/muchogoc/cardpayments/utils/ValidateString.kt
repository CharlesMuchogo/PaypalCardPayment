package com.muchogoc.cardpayments.utils

class ValidateString {
    fun execute(input: String): ValidationResult {
        return if (input.isEmpty()) {
            ValidationResult(
                isSuccessful = false,
                errorMessage = "required",
            )
        } else {
            ValidationResult(
                isSuccessful = true,
            )
        }
    }
}
