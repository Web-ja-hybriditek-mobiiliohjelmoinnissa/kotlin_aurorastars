package fi.antero.aurorastars.data.source.location

import android.annotation.SuppressLint
import android.content.Context
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import fi.antero.aurorastars.data.model.common.LocationInfo
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class LocationDataSource(private val context: Context) {

    @SuppressLint("MissingPermission")
    suspend fun getCurrentLocation(): LocationInfo? {
        val client = LocationServices.getFusedLocationProviderClient(context)

        return suspendCancellableCoroutine { cont ->

            val cancellationTokenSource = CancellationTokenSource()


            client.getCurrentLocation(
                Priority.PRIORITY_HIGH_ACCURACY,
                cancellationTokenSource.token
            ).addOnSuccessListener { loc ->
                if (loc != null) {
                    cont.resume(
                        LocationInfo(
                            latitude = loc.latitude,
                            longitude = loc.longitude
                        )
                    )
                } else {
                    cont.resume(null)
                }
            }.addOnFailureListener {
                cont.resume(null)
            }

            cont.invokeOnCancellation {
                cancellationTokenSource.cancel()
            }
        }
    }
}