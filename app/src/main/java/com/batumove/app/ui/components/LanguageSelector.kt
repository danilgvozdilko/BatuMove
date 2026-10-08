package com.batumove.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.batumove.app.R
import com.batumove.app.domain.model.AppLanguage

@Composable
fun LanguageSelector(
    selectedLanguage: AppLanguage?,
    onLanguageSelected: (AppLanguage) -> Unit,
    enabled: Boolean,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement =
            Arrangement.spacedBy(12.dp),
    ) {

        LanguageItem(
            code = "EN",
            name = stringResource(
                R.string.language_english
            ),
            selected =
                selectedLanguage ==
                    AppLanguage.ENGLISH,
            enabled = enabled,
            onClick = {
                onLanguageSelected(
                    AppLanguage.ENGLISH
                )
            },
        )

        LanguageItem(
            code = "KA",
            name = stringResource(
                R.string.language_georgian
            ),
            selected =
                selectedLanguage ==
                    AppLanguage.GEORGIAN,
            enabled = enabled,
            onClick = {
                onLanguageSelected(
                    AppLanguage.GEORGIAN
                )
            },
        )

        LanguageItem(
            code = "UK",
            name = stringResource(
                R.string.language_ukrainian
            ),
            selected =
                selectedLanguage ==
                    AppLanguage.UKRAINIAN,
            enabled = enabled,
            onClick = {
                onLanguageSelected(
                    AppLanguage.UKRAINIAN
                )
            },
        )

        LanguageItem(
            code = "RU",
            name = stringResource(
                R.string.language_russian
            ),
            selected =
                selectedLanguage ==
                    AppLanguage.RUSSIAN,
            enabled = enabled,
            onClick = {
                onLanguageSelected(
                    AppLanguage.RUSSIAN
                )
            },
        )
    }
}

@Composable
private fun LanguageItem(
    code: String,
    name: String,
    selected: Boolean,
    enabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val borderColor =
        if (selected) {
            MaterialTheme.colorScheme.primary
        } else {
            MaterialTheme.colorScheme.outlineVariant
        }

    val backgroundColor =
        if (selected) {
            MaterialTheme.colorScheme.primaryContainer
                .copy(alpha = 0.28f)
        } else {
            MaterialTheme.colorScheme.surface
        }

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clip(
                RoundedCornerShape(18.dp)
            )
            .clickable(
                enabled = enabled,
                onClick = onClick,
            ),
        shape = RoundedCornerShape(18.dp),
        color = backgroundColor,
        border =
            androidx.compose.foundation.BorderStroke(
                width =
                    if (selected) {
                        2.dp
                    } else {
                        1.dp
                    },
                color = borderColor,
            ),
    ) {

        Row(
            modifier = Modifier.padding(
                horizontal = 16.dp,
                vertical = 14.dp,
            ),
            verticalAlignment =
                Alignment.CenterVertically,
        ) {

            LanguageCode(
                code = code,
                selected = selected,
            )

            Spacer(
                modifier = Modifier.width(16.dp)
            )

            Text(
                text = name,
                style =
                    MaterialTheme.typography.titleLarge,
                color =
                    MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.weight(1f),
            )

            SelectionIndicator(
                selected = selected
            )
        }
    }
}

@Composable
private fun LanguageCode(
    code: String,
    selected: Boolean,
) {
    val background =
        if (selected) {
            MaterialTheme.colorScheme.primary
        } else {
            MaterialTheme.colorScheme.surfaceVariant
        }

    val foreground =
        if (selected) {
            MaterialTheme.colorScheme.onPrimary
        } else {
            MaterialTheme.colorScheme.onSurfaceVariant
        }

    Box(
        modifier = Modifier
            .size(46.dp)
            .clip(
                RoundedCornerShape(14.dp)
            )
            .background(background),
        contentAlignment = Alignment.Center,
    ) {

        Text(
            text = code,
            style =
                MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = foreground,
        )
    }
}

@Composable
private fun SelectionIndicator(
    selected: Boolean,
) {
    Box(
        modifier = Modifier
            .size(24.dp)
            .clip(CircleShape)
            .background(
                if (selected) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.surfaceVariant
                }
            ),
        contentAlignment = Alignment.Center,
    ) {

        if (selected) {
            Text(
                text = "✓",
                color =
                    MaterialTheme.colorScheme.onPrimary,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}