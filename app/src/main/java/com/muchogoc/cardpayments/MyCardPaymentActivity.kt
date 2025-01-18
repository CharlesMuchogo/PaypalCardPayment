package com.muchogoc.cardpayments

import android.app.ComponentCaller
import android.content.Intent
import androidx.fragment.app.FragmentActivity
import com.paypal.android.cardpayments.CardApproveOrderCallback
import com.paypal.android.cardpayments.CardApproveOrderResult
import com.paypal.android.cardpayments.CardRequest
import com.paypal.android.corepayments.PayPalSDKError

class MyCardPaymentActivity : FragmentActivity(), CardApproveOrderCallback {
    override fun onNewIntent(newIntent: Intent, caller: ComponentCaller) {
        super.onNewIntent(intent, caller)
        intent = newIntent
    }
    override fun onCardApproveOrderResult(result: CardApproveOrderResult) {
        println("Card approval result -> $result")
    }

    fun cardCheckoutTapped(cardRequest: CardRequest) {
        val result = cardClient.approveOrder(cardRequest, callback = this)
        println("Card approval result -> $result")
    }
    fun setupCardClient() {

    }

    fun onApproveOrderSuccess(result: CardApproveOrderResult) {
        println("Card approval onApproveOrderSuccess result -> $result")
        // order was approved and is ready to be captured/authorized (see step 6)
    }
    fun onApproveOrderFailure(error: PayPalSDKError) {
        error.printStackTrace()
        println("Card approval error -> ${error.message} ${error.errorDescription} ${error.localizedMessage} ${error.localizedMessage} ")
    }
    fun onApproveOrderCanceled() {
        println("Card approval cancelled ->")
        // 3D Secure flow was canceled
    }
    fun onApproveOrderThreeDSecureWillLaunch() {
        println("Card approval result -> 3d secure will launch")
        // 3D Secure flow will launch
    }
    fun onApproveOrderThreeDSecureDidFinish() {
        println("Card approval result -> 3d didn't finish")
        // 3D Secure auth did finish successfully
    }

}