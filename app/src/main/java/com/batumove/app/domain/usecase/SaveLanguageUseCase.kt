package com.batumove.app.domain.usecase

import com.batumove.app.domain.model.AppLanguage
import com.batumove.app.domain.repository.AppPreferencesRepository
import javax.inject.Inject

class SaveLanguageUseCase @Inject constructor(
    private val repository:
        AppPreferencesRepository,
) {

    suspend operator fun invoke(
        language: AppLanguage,
    ) {
        repository.saveLanguage(language)
    }
}