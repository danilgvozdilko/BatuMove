package com.batumove.app.di

import com.batumove.app.data.remote.api.BatBusRealtimeApi
import com.batumove.app.data.remote.api.BatBusStaticApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import javax.inject.Singleton

private const val STATIC_BASE_URL = "https://batbus.app/"
private const val REALTIME_BASE_URL = "https://api.batbus.app/"

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideJson(): Json =
        Json {
            ignoreUnknownKeys = true
            isLenient = true
        }

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BASIC
        }

        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
    }

    @Provides
    @Singleton
    @StaticApi
    fun provideStaticRetrofit(
        json: Json,
        client: OkHttpClient,
    ): Retrofit =
        Retrofit.Builder()
            .baseUrl(STATIC_BASE_URL)
            .client(client)
            .addConverterFactory(
                json.asConverterFactory(
                    "application/json".toMediaType()
                )
            )
            .build()

    @Provides
    @Singleton
    @RealtimeApi
    fun provideRealtimeRetrofit(
        json: Json,
        client: OkHttpClient,
    ): Retrofit =
        Retrofit.Builder()
            .baseUrl(REALTIME_BASE_URL)
            .client(client)
            .addConverterFactory(
                json.asConverterFactory(
                    "application/json".toMediaType()
                )
            )
            .build()

    @Provides
    @Singleton
    fun provideStaticApi(
        @StaticApi retrofit: Retrofit,
    ): BatBusStaticApi =
        retrofit.create(BatBusStaticApi::class.java)

    @Provides
    @Singleton
    fun provideRealtimeApi(
        @RealtimeApi retrofit: Retrofit,
    ): BatBusRealtimeApi =
        retrofit.create(BatBusRealtimeApi::class.java)
}