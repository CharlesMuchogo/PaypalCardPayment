package com.muchogoc.cardpayments.presentation.common

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp

@Composable
fun AppButton(
    enabled: Boolean = true,
    modifier: Modifier = Modifier.padding(vertical = 10.dp).fillMaxWidth(),
    onClick: () -> Unit,
    shape: Shape? = MaterialTheme.shapes.medium,
    content: @Composable () -> Unit,
) {
    Button(
        enabled = enabled,
        shape = shape ?: ButtonDefaults.shape,
        onClick = onClick,
        modifier = modifier.height(54.dp),
    ) {
        content()
    }
}
