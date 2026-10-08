package com.batumove.app.domain.usecase

import com.batumove.app.domain.model.AppTheme
import com.batumove.app.domain.repository.AppPreferencesRepository
import javax.inject.Inject

class GetThemeUseCase @Inject constructor(
    private val repository: AppPreferencesRepository,
) {

    suspend operator fun invoke(): AppTheme {
        return repository.getTheme()
    }
}