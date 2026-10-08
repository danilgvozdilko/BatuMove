package com.batumove.app.presentation.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.batumove.app.R
import com.batumove.app.domain.model.AppLanguage
import com.batumove.app.domain.model.AppTheme
import com.batumove.app.ui.components.LanguageSelector
import com.batumove.app.ui.components.routes.BatuHeader
import com.batumove.app.ui.theme.BatuMoveDimens

@Composable
fun SettingsRoute(
    modifier: Modifier = Modifier,
    viewModel: SettingsViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    SettingsScreen(
        uiState = uiState,
        onLanguageSelected = viewModel::selectLanguage,
        modifier = modifier,
        onThemeSelected = viewModel::selectTheme,
    )
}

@Composable
fun SettingsScreen(
    uiState: SettingsUiState,
    onLanguageSelected: (AppLanguage) -> Unit,
    modifier: Modifier = Modifier,
    onThemeSelected: (AppTheme) -> Unit,

    ) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(
                horizontal = BatuMoveDimens.screenHorizontalPadding
            )
    ) {

        Spacer(Modifier.height(24.dp))

        BatuHeader(
            stringResource(R.string.navigation_settings)
        )

        Spacer(Modifier.height(24.dp))

        SettingsCard {

            SettingsSectionTitle(
                title = stringResource(R.string.language),
                subtitle = stringResource(R.string.language_title)
            )

            Spacer(Modifier.height(12.dp))

            LanguageSelector(
                selectedLanguage = uiState.selectedLanguage,
                onLanguageSelected = onLanguageSelected,
                enabled = !uiState.isSaving,
                modifier = Modifier
            )

            SettingsDivider()

            SettingsSectionTitle(
                title = stringResource(R.string.theme_title),
                subtitle = stringResource(R.string.theme_subtitle)
            )

            Spacer(Modifier.height(12.dp))

            ThemeOption(
                title = stringResource(R.string.light_theme),
                icon = Icons.Default.LightMode,
                selected = uiState.selectedTheme == AppTheme.LIGHT,
                onClick = {
                    onThemeSelected(AppTheme.LIGHT)
                },
            )

            ThemeOption(
                title = stringResource(R.string.dark_theme),
                icon = Icons.Default.DarkMode,
                selected = uiState.selectedTheme == AppTheme.DARK,
                onClick = {
                    onThemeSelected(AppTheme.DARK)
                },
            )

        }

        Spacer(Modifier.height(24.dp))
    }
}

@Composable
private fun SettingsDivider() {
    HorizontalDivider(
        modifier = Modifier.padding(vertical = 20.dp),
        color = MaterialTheme.colorScheme.outlineVariant
    )
}

@Composable
private fun ThemeOption(
    title: String,
    icon: ImageVector,
    selected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .clickable(onClick = onClick)
            .padding(
                horizontal = 12.dp,
                vertical = 10.dp
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(
                    if (selected) {
                        MaterialTheme.colorScheme.primaryContainer
                    } else {
                        MaterialTheme.colorScheme.surfaceVariant
                    }
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (selected) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant
                },
                modifier = Modifier.size(21.dp)
            )
        }

        Spacer(Modifier.width(12.dp))

        Text(
            text = title,
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface
        )

        RadioButton(
            selected = selected,
            onClick = onClick
        )
    }
}

@Composable
private fun SettingsSectionTitle(
    title: String,
    subtitle: String
) {
    Column {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(Modifier.height(4.dp))

        Text(
            text = subtitle,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun SettingsCard(
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            content = content
        )
    }
}

@Preview
@Composable
fun PreviewSettings() {
    SettingsRoute()
}
