package com.batumove.app.di

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class StaticApi

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class RealtimeApi