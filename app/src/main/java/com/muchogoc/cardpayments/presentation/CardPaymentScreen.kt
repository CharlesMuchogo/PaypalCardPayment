package com.muchogoc.cardpayments.presentation

import android.content.Context
import android.content.ContextWrapper
import androidx.activity.ComponentActivity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.muchogoc.blogapp.presentation.utils.ResultStatus
import com.muchogoc.cardpayments.R
import com.muchogoc.cardpayments.presentation.actions.PaymentCardDetailEntryAction
import com.muchogoc.cardpayments.presentation.common.AppButton
import com.muchogoc.cardpayments.presentation.common.AppTextField
import com.muchogoc.cardpayments.presentation.common.AppTextFieldValueTextField
import com.muchogoc.cardpayments.presentation.viewmodel.CardPaymentViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun CardPaymentScreen(
    modifier: Modifier = Modifier
){
    val cardPaymentViewModel = koinViewModel<CardPaymentViewModel>()
    val showCVVDetails = cardPaymentViewModel.showCVVDetails
    val cardDetailsState = cardPaymentViewModel.cardDetailsState
    val cardTypeIcon = cardPaymentViewModel.cardTypeIcon

    val cardPaymentState by cardPaymentViewModel.cardPaymentState.collectAsState()

    val context = LocalContext.current

    var isFormating by remember { mutableStateOf(false) }

    var isFormattingCVV by remember { mutableStateOf(false) }

    val cardAlpha by animateFloatAsState(
        if (showCVVDetails) 180f else 0f,
        label = "card alpha",
        animationSpec = tween(
            durationMillis = 1000
        )
    )

    val cardColor by animateColorAsState(
        if (cardTypeIcon != null) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
        label = "card color",
        animationSpec = tween(
            durationMillis = 700
        )
    )

    Column(
        verticalArrangement = Arrangement.spacedBy(2.dp, alignment = Alignment.CenterVertically),
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .fillMaxSize()
            .padding(horizontal = 12.dp)
            .padding(top = 12.dp)
            .zIndex(0.9f)
    ) {
        Box(
            modifier = Modifier
                .graphicsLayer {
                    this.rotationY = cardAlpha
                }
                .fillMaxWidth()
                .height(220.dp)
                .clip(
                    shape = RoundedCornerShape(16.dp)
                )
                .background(cardColor)
                .padding(16.dp)
                .zIndex(1.0f)

        ) {
            if (showCVVDetails){

                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .background(MaterialTheme.colorScheme.onBackground)
                        .align(Alignment.Center)
                )

                Text(
                    text = if (cardDetailsState.cvv.isNotEmpty() || cardDetailsState.cvv.isNotBlank()) cardDetailsState.cvv else "***",
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Center,
                    fontFamily = FontFamily.Monospace,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier
                        .width(50.dp)
                        .height(25.dp)
                        .background(MaterialTheme.colorScheme.background)
                        .align(Alignment.BottomStart)
                        .graphicsLayer {
                            this.rotationY = 180f
                        }
                )

            }else {

                Image(
                    painter = painterResource(R.drawable.chip),
                    contentDescription = "card chip",
                    modifier = Modifier
                        .size(60.dp)
                        .align(Alignment.TopStart)
                )


                cardTypeIcon?.let { icon ->
                    Image(
                        painter = painterResource(icon),
                        contentDescription = "",
                        modifier = Modifier
                            .size(40.dp)
                            .align(Alignment.TopEnd)

                    )
                }


                Text(
                    text = if (cardDetailsState.cardNumber.isNotEmpty() || cardDetailsState.cardNumber.isNotBlank()) cardDetailsState.cardNumber else "**** **** **** ****",
                    color = MaterialTheme.colorScheme.onPrimary,
                    style = MaterialTheme.typography.titleLarge,
                    fontFamily = FontFamily.Serif,
                    modifier = Modifier
                        .align(Alignment.Center)
                )


                Text(
                    text = if (cardDetailsState.cardholderName.isNotEmpty() || cardDetailsState.cardholderName.isNotBlank()) cardDetailsState.cardholderName else "Jane Doe",
                    color = MaterialTheme.colorScheme.onPrimary,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                )

                Text(
                    text = if (cardDetailsState.validThru.isNotEmpty() || cardDetailsState.validThru.isNotBlank()) cardDetailsState.validThru else "mm/yy",
                    color = MaterialTheme.colorScheme.onPrimary,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                )
            }

        }

        Spacer(Modifier.padding(8.dp))

        AppTextFieldValueTextField(
            required = true,
            error = cardDetailsState.cardNumberError,
            onValueChanged = {
                if (isFormating) return@AppTextFieldValueTextField

                isFormating = true

                cardPaymentViewModel.onCardPaymentDetailEntryAction(
                    action = PaymentCardDetailEntryAction.OnCardNumberChange(cardNumber = it.text)
                )

                isFormating = false
            },
            label = "Card Number",
            keyboardType = KeyboardType.Number,
            placeholder = "1234 1234 1234 1234",
            value = TextFieldValue(
                text = cardDetailsState.cardNumber,
                selection = TextRange(cardDetailsState.cardNumber.length)
            ),
            trailingIcon = {

                AnimatedVisibility(
                    visible = cardTypeIcon != null,
                    enter = scaleIn(),
                    exit = scaleOut()
                ){
                    cardTypeIcon?.let {icon ->
                        Image(
                            painter = painterResource(icon),
                            contentDescription = "",
                            modifier = Modifier
                                .size(28.dp)
                        )
                    }
                }
            }

        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .fillMaxWidth()
        ) {
            AppTextFieldValueTextField(
                required = true,
                error = cardDetailsState.validThruError,
                onValueChanged = {
                    cardPaymentViewModel.onCardPaymentDetailEntryAction(
                        action = PaymentCardDetailEntryAction.OnValidThruChange(validThru = it.text)
                    )
                },
                keyboardType = KeyboardType.Number,
                label = "Valid Thru",
                placeholder = "mm/yy",
                value = TextFieldValue(
                    text = cardDetailsState.validThru,
                    selection = TextRange(cardDetailsState.validThru.length)
                ),
                modifier = Modifier
                    .width(120.dp)
            )

            AppTextField(
                required = true,
                error = cardDetailsState.cvvError,
                onValueChanged = {
                    if (isFormattingCVV) return@AppTextField

                    isFormattingCVV = true

                    cardPaymentViewModel.onCardPaymentDetailEntryAction(
                        action = PaymentCardDetailEntryAction.OnCVVChange(cvv = it)
                    )

                    isFormattingCVV = false

                },
                label = "CVV",
                placeholder = "***",
                value = cardDetailsState.cvv,
                keyboardType = KeyboardType.NumberPassword,
                passwordVisible = showCVVDetails,
                trailingIcon = {
                    AnimatedVisibility(
                        cardDetailsState.cvv.isNotBlank(),
                        enter = fadeIn(),
                        exit = fadeOut()
                    ){
                        IconButton(
                            onClick = {
                                if (cardDetailsState.cvv.isNotBlank()) {
                                    cardPaymentViewModel.updateCVVVisibility()
                                }
                            },
                            content = {
                                Icon(
                                    imageVector = if(showCVVDetails) Icons.Outlined.Visibility else Icons.Outlined.VisibilityOff,
                                    contentDescription = "show CVV"
                                )
                            }
                        )
                    }


                },
                modifier = Modifier
                    .width(120.dp)

            )
        }

        AppTextField(
            required = true,
            error = cardDetailsState.cardholderNameError,
            onValueChanged = {
                cardPaymentViewModel.onCardPaymentDetailEntryAction(
                    action = PaymentCardDetailEntryAction.OnCardholderNameChange(cardholderName = it)
                )

            },
            label = "Cardholder Name",
            placeholder = "Jane Doe",
            imeAction = ImeAction.Done,
            value = cardDetailsState.cardholderName
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                colors = CheckboxDefaults.colors(
                    checkedColor = MaterialTheme.colorScheme.primary
                ),
                onCheckedChange = {
                    cardPaymentViewModel.onCardPaymentDetailEntryAction(
                        action = PaymentCardDetailEntryAction.OnSaveCardDetailsChange(saveDetails = it)
                    )
                },
                checked = cardDetailsState.saveCardDetails
            )

            Text(
                text = "Save card for future payment",
                style = MaterialTheme.typography.bodyMedium
            )
        }

        cardPaymentState.message?.let {
            Text(it, modifier = Modifier.padding(vertical = 8.dp), style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.error))
        }

        cardPaymentState.data?.let {
            Text(it, modifier = Modifier.padding(vertical = 8.dp), style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary))
        }

        AppButton(
            onClick = {
                context.getActivityOrNull()?.let { cardPaymentViewModel.onCardPaymentDetailEntryAction(PaymentCardDetailEntryAction.OnSubmit(activity = it))}
            },
            content = {
                when(cardPaymentState.status == ResultStatus.LOADING){
                    true -> {
                        println("Loading")
                        CircularProgressIndicator(
                            modifier = Modifier.size(25.dp),
                            strokeWidth = 2.dp,
                            color = MaterialTheme.colorScheme.onPrimary,
                        )

                    }
                    false -> {
                        Text(
                            "Proceed to Pay",
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }


            }
        )
    }

}

fun Context.getActivityOrNull(): ComponentActivity? = when (this) {
    is ComponentActivity -> this
    is ContextWrapper -> baseContext.getActivityOrNull()
    else -> null
}
