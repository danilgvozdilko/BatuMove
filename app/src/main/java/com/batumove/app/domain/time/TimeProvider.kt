package com.batumove.app.domain.time

interface TimeProvider {

    fun currentTimeMillis(): Long
}