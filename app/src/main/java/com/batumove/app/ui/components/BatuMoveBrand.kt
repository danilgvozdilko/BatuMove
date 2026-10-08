package com.batumove.app.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.batumove.app.R

@Composable
fun BatuMoveBrand(
    modifier: Modifier = Modifier,
    showTagline: Boolean = true,
) {
    val brandFirst =
        stringResource(R.string.brand_name_first)

    val brandSecond =
        stringResource(R.string.brand_name_second)

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        BatuMoveLogo(
            modifier = Modifier.size(92.dp)
        )

        Spacer(
            modifier = Modifier.height(18.dp)
        )

        Text(
            text = buildAnnotatedString {

                withStyle(
                    SpanStyle(
                        color = MaterialTheme
                            .colorScheme
                            .onBackground,
                    )
                ) {
                    append(brandFirst)
                }

                withStyle(
                    SpanStyle(
                        color = MaterialTheme
                            .colorScheme
                            .primary,
                    )
                ) {
                    append(brandSecond)
                }
            },
            style = MaterialTheme
                .typography
                .displayLarge,
        )

        if (showTagline) {

            Spacer(
                modifier = Modifier.height(6.dp)
            )

            Text(
                text = stringResource(
                    R.string.brand_tagline
                ),
                color = MaterialTheme
                    .colorScheme
                    .onSurfaceVariant,
                style = MaterialTheme
                    .typography
                    .bodyLarge,
            )
        }
    }
}