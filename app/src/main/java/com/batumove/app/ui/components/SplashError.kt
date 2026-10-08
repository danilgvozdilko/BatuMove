package com.batumove.app.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.batumove.app.R

@Composable
fun SplashError(
    onRetry: () -> Unit,
) {
    Column(
        horizontalAlignment =
            Alignment.CenterHorizontally,
    ) {

        Text(
            text = stringResource(
                R.string.splash_error
            ),
            color =
                MaterialTheme
                    .colorScheme
                    .onSurfaceVariant,
            style =
                MaterialTheme
                    .typography
                    .bodyMedium,
            textAlign = TextAlign.Center,
        )

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        Button(
            onClick = onRetry,
        ) {
            Text(
                text = stringResource(
                    R.string.action_retry
                )
            )
        }
    }
}