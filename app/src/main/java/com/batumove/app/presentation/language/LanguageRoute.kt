package com.batumove.app.presentation.language

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.batumove.app.R
import com.batumove.app.domain.model.AppLanguage
import com.batumove.app.ui.components.BatuMoveLogo
import com.batumove.app.ui.components.BatuMovePrimaryButton
import com.batumove.app.ui.components.LanguageSelector
import com.batumove.app.ui.theme.BatuMoveDimens

@Composable
fun LanguageRoute(
    onFinished: () -> Unit,
    viewModel: LanguageViewModel = hiltViewModel(),
) {
    val uiState by
    viewModel.uiState
        .collectAsStateWithLifecycle()

    LaunchedEffect(
        uiState.completedLanguage
    ) {
        if (
            uiState.completedLanguage != null
        ) {
            onFinished()
        }
    }

    LanguageScreen(
        uiState = uiState,
        onLanguageSelected =
            viewModel::selectLanguage,
        onContinueClick =
            viewModel::continueWithSelectedLanguage,
    )
}

@Composable
fun LanguageScreen(
    uiState: LanguageUiState,
    onLanguageSelected: (AppLanguage) -> Unit,
    onContinueClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(
                MaterialTheme.colorScheme.background
            )
            .padding(
                horizontal =
                    BatuMoveDimens.screenHorizontalPadding,
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        Spacer(
            modifier = Modifier.weight(0.7f)
        )

        BatuMoveLogo(
            modifier = Modifier.size(72.dp)
        )

        Spacer(
            modifier = Modifier.height(28.dp)
        )

        Text(
            text = stringResource(
                R.string.language_title
            ),
            style =
                MaterialTheme.typography.headlineLarge,
            color =
                MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center,
        )

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        Text(
            text = stringResource(
                R.string.language_subtitle
            ),
            style =
                MaterialTheme.typography.bodyLarge,
            color =
                MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )

        Spacer(
            modifier = Modifier.height(36.dp)
        )

        LanguageSelector(
            selectedLanguage =
                uiState.selectedLanguage,
            onLanguageSelected =
                onLanguageSelected,
            enabled = !uiState.isSaving,
        )

        if (uiState.hasError) {

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            Text(
                text = stringResource(
                    R.string.language_save_error
                ),
                style =
                    MaterialTheme.typography.bodyMedium,
                color =
                    MaterialTheme.colorScheme.error,
                textAlign = TextAlign.Center,
            )
        }

        Spacer(
            modifier = Modifier.weight(1f)
        )

        BatuMovePrimaryButton(
            text = stringResource(
                R.string.action_continue
            ),
            enabled =
                uiState.selectedLanguage != null &&
                        !uiState.isSaving,
            loading = uiState.isSaving,
            onClick = onContinueClick,
        )

        Spacer(
            modifier = Modifier.height(40.dp)
        )
    }
}