package com.muchogoc.cardpayments.presentation.actions

import androidx.activity.ComponentActivity

sealed interface PaymentCardDetailEntryAction {
    data class OnSubmit(val activity: ComponentActivity) : PaymentCardDetailEntryAction

    data class OnCardNumberChange(val cardNumber: String): PaymentCardDetailEntryAction

    data class OnValidThruChange(val validThru: String): PaymentCardDetailEntryAction

    data class OnCVVChange(val cvv: String): PaymentCardDetailEntryAction

    data class OnCardholderNameChange(val cardholderName: String): PaymentCardDetailEntryAction

    data class OnSaveCardDetailsChange(val saveDetails: Boolean): PaymentCardDetailEntryAction
}