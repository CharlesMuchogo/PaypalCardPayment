package com.muchogoc.cardpayments.presentation.state

data class PaymentCardDetailsState(
    val cardNumber: String = "",
    val validThru: String = "",
    val cvv: String = "",
    val cardholderName: String = "",
    val saveCardDetails: Boolean = false,
    val cardType: String = "",

    /** error **/
    val cardNumberError: String? = null,
    val cvvError: String? = null,
    val validThruError: String? = null,
    val cardholderNameError: String? = null
)