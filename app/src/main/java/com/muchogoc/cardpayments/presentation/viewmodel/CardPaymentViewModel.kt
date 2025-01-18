package com.muchogoc.cardpayments.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.muchogoc.blogapp.presentation.utils.Results
import com.muchogoc.cardpayments.MyCardPaymentActivity
import com.muchogoc.cardpayments.R
import com.muchogoc.cardpayments.cardClient
import com.muchogoc.cardpayments.data.remote.RemoteRepository
import com.muchogoc.cardpayments.domain.dto.Amount
import com.muchogoc.cardpayments.domain.dto.GetAccessTokenResponseDTO
import com.muchogoc.cardpayments.domain.dto.GetOrderIdResponseDTO
import com.muchogoc.cardpayments.domain.dto.OrderIdRequest
import com.muchogoc.cardpayments.domain.dto.PurchaseUnit
import com.muchogoc.cardpayments.presentation.actions.PaymentCardDetailEntryAction
import com.muchogoc.cardpayments.presentation.state.PaymentCardDetailsState
import com.muchogoc.cardpayments.utils.Utils
import com.muchogoc.cardpayments.utils.ValidateString
import com.paypal.android.cardpayments.Card
import com.paypal.android.cardpayments.CardRequest
import com.paypal.android.cardpayments.threedsecure.SCA
import com.paypal.android.corepayments.Address
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class CardPaymentViewModel(private val remoteRepository: RemoteRepository): ViewModel() {

    private val getAccessTokenState = MutableStateFlow(Results.initial<GetAccessTokenResponseDTO>())
    private val getOrderIdState = MutableStateFlow(Results.initial<GetOrderIdResponseDTO>())


    var showCVVDetails by mutableStateOf(false)

    fun updateCVVVisibility(){
        showCVVDetails = !showCVVDetails
    }

    var cardDetailsState by mutableStateOf(PaymentCardDetailsState())

    var cardTypeIcon by mutableStateOf<Int?>(null)

    fun onCardPaymentDetailEntryAction(action: PaymentCardDetailEntryAction){
        when(action){
            is PaymentCardDetailEntryAction.OnCVVChange -> {

                if (action.cvv.length < 5) {
                    cardDetailsState = cardDetailsState.copy(
                        cvv = action.cvv
                    )
                }
            }
            is PaymentCardDetailEntryAction.OnCardNumberChange -> {
                val formatted = Utils.formatCardNumber(cardNumber = action.cardNumber.replace("\\s".toRegex(), ""))

                val cardType = Utils.detectCardType(cardDetailsState.cardNumber.replace("\\s".toRegex(), ""))

                cardDetailsState = cardDetailsState.copy(
                    cardNumber = formatted,
                    cardType = cardType
                )
                cardTypeIcon = when(cardType){
                    "Visa" -> R.drawable.visa
                    "Mastercard" -> R.drawable.mastercard_symbol
                    else -> {null}
                }
            }
            is PaymentCardDetailEntryAction.OnCardholderNameChange -> {
                cardDetailsState = cardDetailsState.copy(
                    cardholderName = action.cardholderName
                )
            }
            is PaymentCardDetailEntryAction.OnValidThruChange -> {
                if (action.validThru.length < 6) {

                    val formatted = Utils.formatValidThru(validThru = action.validThru.replace("/", ""))
                    cardDetailsState = cardDetailsState.copy(
                        validThru = formatted
                    )
                }
            }

            is PaymentCardDetailEntryAction.OnSaveCardDetailsChange -> {
                cardDetailsState = cardDetailsState.copy(
                    saveCardDetails = action.saveDetails
                )
            }


            PaymentCardDetailEntryAction.OnSubmit -> { initiatePayment(cardDetailsState = cardDetailsState) }
        }
    }


    private val validateString = ValidateString()

    private fun initiatePayment(cardDetailsState: PaymentCardDetailsState){

        viewModelScope.launch {

            getAccessTokenState.value = remoteRepository.getAccessToken()

            val purchaseUnits = listOf(
                PurchaseUnit(
                amount = Amount(
                    currency_code = "USD",
                    value = "5"
                )
            )
            )


            if (getAccessTokenState.value.data?.access_token == null){
                return@launch
            }

            val orderIdRequest = OrderIdRequest(
                intent = "CAPTURE",
                purchase_units = purchaseUnits
            )

            getOrderIdState.value = remoteRepository.getOrderId(orderIdRequest, getAccessTokenState.value.data?.access_token!!)


            if (getOrderIdState.value.data?.id == null){
                return@launch
            }


            val card = Card(
                number = "4032032097574757",
                expirationMonth = "09",
                expirationYear = "2027",
                securityCode = "838",
                cardholderName = "Charles Muchogo",
                billingAddress = Address(
                    streetAddress = "1600 Amphitheatre Parkway", // A valid street address
                    extendedAddress = "Building 43",            // Optional extended address
                    locality = "Mountain View",                 // City
                    region = "CA",
                    postalCode = "94043",// State (use 2-letter state codes in the US)
                    countryCode = "US"                          // ISO country code
                )
            )

            println("OrderId is ${getOrderIdState.value.data?.id}")

            val cardRequest  = CardRequest(
                orderId = getOrderIdState.value.data?.id!!,
                card = card,
                returnUrl = "myapp://paypal/callback",
                sca = SCA.SCA_ALWAYS
            )


            cardClient.approveOrder(cardRequest = cardRequest, callback = MyCardPaymentActivity())

        }
    }

}