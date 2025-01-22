package com.muchogoc.cardpayments.presentation.state

data class PaymentCardDetailsState(
    val cardNumber: String = "4111111111111111",
    val validThru: String = "01/27",
    val cvv: String = "123",
    val cardholderName: String = "Charles Muchogo",
    val saveCardDetails: Boolean = false,
    val cardType: String = "Visa",

    /** error **/
    val cardNumberError: String? = null,
    val cvvError: String? = null,
    val validThruError: String? = null,
    val cardholderNameError: String? = null
)