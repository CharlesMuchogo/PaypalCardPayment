package com.muchogoc.cardpayments.utils

object Utils {

    fun decodeExceptionMessage(e: Exception): String {
        e.printStackTrace()
        if (e.message?.lowercase()?.contains("failed to connect") == true) {
            return "Check your Internet connection and try again"
        }
        return "Something went wrong. Try again"
    }

    fun formatCardNumber(cardNumber: String): String {
        return cardNumber.chunked(4).joinToString(" ")
    }

    fun detectCardType(cardNumber: String): String {
        return when {
            cardNumber.startsWith("4") && cardNumber.length in 13..19 -> "Visa"
            cardNumber.startsWith("5") && cardNumber.length == 16 -> "Mastercard"
            cardNumber.startsWith("34") || cardNumber.startsWith("37") && cardNumber.length == 15 -> "American Express"
            cardNumber.startsWith("6") && cardNumber.length == 16 -> "Discover"
            cardNumber.startsWith("35") && cardNumber.length == 16 -> "JCB"
            cardNumber.startsWith("30") || cardNumber.startsWith("36") || cardNumber.startsWith("38") && cardNumber.length == 14 -> "Diners Club"
            else -> "Unknown"
        }
    }

    fun formatValidThru(validThru: String): String {
        return validThru.chunked(2).joinToString("/")
    }

    fun isValidPhoneNumber(phone: String): Boolean {
        val phoneRegex = Regex("^\\+?[0-9]{10,15}\$")
        return phone.matches(phoneRegex)
    }

}