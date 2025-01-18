package com.muchogoc.cardpayments

import android.app.Application
import com.muchogoc.cardpayments.data.local.PreferenceManager.CLIENT_ID
import com.muchogoc.cardpayments.di.KoinInit
import com.muchogoc.cardpayments.di.commonModule
import com.paypal.android.cardpayments.CardClient
import com.paypal.android.corepayments.CoreConfig
import com.paypal.android.corepayments.Environment
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.logger.Level


class CardPayments: Application() {
    override fun onCreate() {
        super.onCreate()

        KoinInit().init {
            androidLogger(level =  Level.ERROR )
            androidContext(androidContext = this@CardPayments)
            modules(
                listOf(
                    commonModule(),
                ),
            )



        }
    }
}