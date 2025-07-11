package com.example.myfoodsapp.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Looper
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.Priority
import java.io.IOException
import java.util.Locale

class LocationHelper(
    private val context: Context,
    private val fusedLocationClient: FusedLocationProviderClient
) {

    fun getUserLocation(onResult: (String) -> Unit, onError: (String) -> Unit) {
        val locationRequest = LocationRequest.create().apply {
            priority = Priority.PRIORITY_BALANCED_POWER_ACCURACY
            interval = 0
            fastestInterval = 0
            numUpdates = 1
        }

        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            onError("İcazə verilməyib")
            return
        }

        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    val location = locationResult.lastLocation
                    if (location != null) {
                        getCityName(location.latitude, location.longitude, onResult, onError)
                    } else {
                        onError("Location tapılmadı")
                    }
                }
            },
            Looper.getMainLooper()
        )
    }

    private fun getCityName(
        lat: Double,
        lon: Double,
        onResult: (String) -> Unit,
        onError: (String) -> Unit
    ) {
        try {
            val geocoder = Geocoder(context, Locale.getDefault())
            val addresses = geocoder.getFromLocation(lat, lon, 1)

            val text = if (!addresses.isNullOrEmpty()) {
                val city = addresses[0].locality.orEmpty()
                val country = addresses[0].countryName.orEmpty()
                "$city, $country"
            } else {
                "Bilinmir"
            }

            onResult(text)
        } catch (e: IOException) {
            onError("Şəhər tapılmadı")
        }
    }
}
