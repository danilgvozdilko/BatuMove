package com.batumove.app.ui.components.routes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.batumove.app.R
import com.batumove.app.ui.components.BatuMovePrimaryButton

@Composable
fun RoutesError(
    onRetry: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment =
            Alignment.CenterHorizontally,
        verticalArrangement =
            Arrangement.Center,
    ) {

        Text(
            text =
                stringResource(
                    R.string.routes_error
                ),
            color =
                MaterialTheme.colorScheme.onSurfaceVariant,
        )

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        BatuMovePrimaryButton(
            text = stringResource(
                R.string.action_retry
            ),
            onClick = onRetry,
        )
    }
}