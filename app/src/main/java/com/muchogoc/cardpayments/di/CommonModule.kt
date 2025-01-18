package com.muchogoc.cardpayments.di

import com.muchogoc.cardpayments.data.remote.RemoteRepository
import com.muchogoc.cardpayments.data.remote.RemoteRepositoryImpl
import com.muchogoc.cardpayments.presentation.viewmodel.CardPaymentViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

fun commonModule() =
    module {

       single<RemoteRepository>{
           RemoteRepositoryImpl()
       }

       viewModelOf(::CardPaymentViewModel)
    }