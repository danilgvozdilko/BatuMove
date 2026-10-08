package com.batumove.app.domain.usecase

import com.batumove.app.domain.model.AppLanguage
import com.batumove.app.domain.repository.AppPreferencesRepository
import javax.inject.Inject

class GetLanguageUseCase @Inject constructor(
    private val repository: AppPreferencesRepository,
) {

    suspend operator fun invoke(): AppLanguage? {
        return repository.getLanguage()
    }
}