package com.muchogoc.cardpayments

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import com.muchogoc.cardpayments.data.local.PreferenceManager.CLIENT_ID
import com.muchogoc.cardpayments.presentation.CardPaymentOptionBottomSheet
import com.muchogoc.cardpayments.ui.theme.CardPaymentsTheme
import com.paypal.android.cardpayments.CardClient
import com.paypal.android.corepayments.CoreConfig
import com.paypal.android.corepayments.Environment

lateinit var cardClient: CardClient

class MainActivity : ComponentActivity() {


    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val config = CoreConfig(CLIENT_ID, environment = Environment.SANDBOX)
        cardClient = CardClient(this, config)
        enableEdgeToEdge()
        setContent {
            CardPaymentsTheme {
                Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
                    CenterAlignedTopAppBar(title = { Text("Checkout") })
                }) { innerPadding ->
                    CardPaymentOptionBottomSheet(Modifier.padding(innerPadding))
                }
            }
        }
    }
}
