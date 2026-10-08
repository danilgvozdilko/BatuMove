package com.batumove.app.domain.usecase

import com.batumove.app.domain.repository.AppPreferencesRepository
import javax.inject.Inject

class CompleteOnboardingUseCase @Inject constructor(
    private val repository:
        AppPreferencesRepository,
) {

    suspend operator fun invoke() {
        repository.completeOnboarding()
    }
}