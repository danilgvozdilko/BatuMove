package com.batumove.app.data.location

import android.annotation.SuppressLint
import android.content.Context
import com.batumove.app.domain.model.GeoPoint
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume

@Singleton
class LocationDataSourceImpl @Inject constructor(
    @ApplicationContext context: Context,
) : LocationDataSource {

    private val client =
        LocationServices
            .getFusedLocationProviderClient(context)

    @SuppressLint("MissingPermission")
    override suspend fun getCurrentLocation(): GeoPoint? =
        suspendCancellableCoroutine { continuation ->

            client
                .getCurrentLocation(
                    Priority.PRIORITY_HIGH_ACCURACY,
                    null,
                )
                .addOnSuccessListener { location ->

                    continuation.resume(
                        location?.let {
                            GeoPoint(
                                latitude = it.latitude,
                                longitude = it.longitude,
                            )
                        }
                    )
                }
                .addOnFailureListener {
                    continuation.resume(null)
                }
        }
}