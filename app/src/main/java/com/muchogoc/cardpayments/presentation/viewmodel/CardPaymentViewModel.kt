package com.muchogoc.cardpayments.presentation.viewmodel

import androidx.activity.ComponentActivity
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.muchogoc.blogapp.presentation.utils.Results
import com.muchogoc.cardpayments.R
import com.muchogoc.cardpayments.data.local.PreferenceManager.CLIENT_ID
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
import com.paypal.android.cardpayments.CardApproveOrderResult
import com.paypal.android.cardpayments.CardAuthChallenge
import com.paypal.android.cardpayments.CardClient
import com.paypal.android.cardpayments.CardPresentAuthChallengeResult
import com.paypal.android.cardpayments.CardRequest
import com.paypal.android.cardpayments.threedsecure.SCA
import com.paypal.android.corepayments.CoreConfig
import com.paypal.android.corepayments.Environment
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class CardPaymentViewModel(private val remoteRepository: RemoteRepository): ViewModel() {

    private val getAccessTokenState = MutableStateFlow(Results.initial<GetAccessTokenResponseDTO>())
    private val getOrderIdState = MutableStateFlow(Results.initial<GetOrderIdResponseDTO>())
    val cardPaymentState = MutableStateFlow(Results.initial<String>())


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


            PaymentCardDetailEntryAction.OnSubmit -> {  }
        }
    }


    private val validateString = ValidateString()

     fun initiatePayment(activity: ComponentActivity){

        viewModelScope.launch {
            cardPaymentState.value = Results.loading()
            getAccessTokenState.value = remoteRepository.getAccessToken()

            val purchaseUnits = listOf(
                PurchaseUnit(
                amount = Amount(
                    currency_code = "USD",
                    value = "5.00"
                )
            )
            )


            if (getAccessTokenState.value.data?.access_token == null){
                cardPaymentState.value = Results.error(getAccessTokenState.value.message ?: "Failed to get Access token")
                return@launch
            }

            val orderIdRequest = OrderIdRequest(
                intent = "CAPTURE",
                purchase_units = purchaseUnits
            )

            getOrderIdState.value = remoteRepository.getOrderId(orderIdRequest, getAccessTokenState.value.data?.access_token!!)

            if(getOrderIdState.value.data?.id == null){
                cardPaymentState.value = Results.error(getAccessTokenState.value.message ?: "Failed to get Access token")
                return@launch
            }

            val orderId = getOrderIdState.value.data?.id!!

            println(getOrderIdState.value.data)


            val card = Card(
                number = "4111111111111111",
                expirationMonth = "01",
                expirationYear = "2027",
                securityCode = "123",
            )



            println("OrderId is $orderId")
            println("my card -> $card")

            val cardRequest  = CardRequest(
                orderId = orderId,
                card = card,
                returnUrl = "com.muchogoc.cardpayments://example.com/returnUrl",
                sca = SCA.SCA_WHEN_REQUIRED
            )


            val config = CoreConfig(CLIENT_ID, environment = Environment.SANDBOX)
            val cardClient = CardClient(activity ,config)

            cardClient.approveOrder(cardRequest = cardRequest){ result ->
                when (result) {
                    is CardApproveOrderResult.Success -> {
                        cardPaymentState.value = Results.success("Transaction Successfully approved")
                    }

                    is CardApproveOrderResult.AuthorizationRequired -> {
                        presentAuthChallenge(activity, authChallenge = result.authChallenge, cardClient = cardClient)
                    }

                    is CardApproveOrderResult.Failure -> {
                        cardPaymentState.value = Results.error(result.error.message ?: "Something went wrong approving order")
                    }
                }
            }
        }
    }

    private fun presentAuthChallenge(
        activity: ComponentActivity,
        cardClient: CardClient,
        authChallenge: CardAuthChallenge
    ) {
        cardClient.presentAuthChallenge(activity, authChallenge).let { presentAuthResult ->
            when (presentAuthResult) {
                is CardPresentAuthChallengeResult.Success -> {
                    //authState = presentAuthResult.authState
                }

                is CardPresentAuthChallengeResult.Failure -> {
                    //approveOrderState = ActionState.Failure(presentAuthResult.error)
                }
            }
        }
    }

}