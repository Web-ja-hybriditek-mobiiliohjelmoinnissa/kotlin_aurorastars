package fi.antero.aurorastars.data.source.location

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.os.Build
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import java.util.Locale
import kotlin.coroutines.resume

class GeocoderDataSource(private val context: Context) {

    suspend fun getPlaceName(lat: Double, lon: Double): String {
        return withContext(Dispatchers.IO) {
            try {
                val geocoder = Geocoder(context, Locale.getDefault())

                val addresses: List<Address> = if (Build.VERSION.SDK_INT >= 33) {
                    suspendCancellableCoroutine { cont ->
                        geocoder.getFromLocation(lat, lon, 1) { list ->
                            cont.resume(list ?: emptyList())
                        }
                    }
                } else {
                    @Suppress("DEPRECATION")
                    geocoder.getFromLocation(lat, lon, 1) ?: emptyList()
                }

                if (addresses.isNotEmpty()) {
                    val a: Address = addresses[0]
                    val locality: String? = a.locality
                    val subAdmin: String? = a.subAdminArea
                    val admin: String? = a.adminArea
                    locality ?: subAdmin ?: admin ?: "LOCATION_FALLBACK"
                } else {
                    "LOCATION_FALLBACK"
                }
            } catch (e: Exception) {
                "LOCATION_FALLBACK"
            }
        }
    }
}