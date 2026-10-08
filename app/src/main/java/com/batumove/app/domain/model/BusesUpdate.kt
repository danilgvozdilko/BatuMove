package com.batumove.app.domain.model

sealed interface BusesUpdate {

    data class Success(
        val buses: List<Bus>,
    ) : BusesUpdate

    data class Error(
        val cause: Throwable,
    ) : BusesUpdate
}