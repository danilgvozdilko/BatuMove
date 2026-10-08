package com.batumove.app.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.batumove.app.R
import com.batumove.app.ui.theme.BatuMoveDimens

@Composable
fun BatuMoveTopBar(
    modifier: Modifier = Modifier, applySystemInsets: Boolean = true,

    ) {
    val firstPart =
        stringResource(R.string.brand_name_first)

    val secondPart =
        stringResource(R.string.brand_name_second)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(
                horizontal =
                    BatuMoveDimens.screenHorizontalPadding,
                vertical = 12.dp,
            ),
    ) {

        Text(
            text = buildAnnotatedString {

                withStyle(
                    SpanStyle(
                        color = MaterialTheme
                            .colorScheme
                            .onBackground,
                        fontWeight = FontWeight.Bold,
                    )
                ) {
                    append(firstPart)
                }

                withStyle(
                    SpanStyle(
                        color = MaterialTheme
                            .colorScheme
                            .primary,
                        fontWeight = FontWeight.Bold,
                    )
                ) {
                    append(secondPart)
                }
            },
            style =
                MaterialTheme.typography.headlineMedium,
        )
    }
}