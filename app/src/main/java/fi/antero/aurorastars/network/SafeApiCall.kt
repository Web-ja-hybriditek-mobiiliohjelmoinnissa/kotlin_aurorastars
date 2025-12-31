package fi.antero.aurorastars.network

import fi.antero.aurorastars.util.Result
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.RedirectResponseException
import io.ktor.client.plugins.ServerResponseException
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException


suspend fun <T> safeApiCall(apiCall: suspend () -> T): Result<T> {
    return try {
        Result.Success(apiCall())
    } catch (e: Exception) {
        val errorMessage = when (e) {

            is UnknownHostException -> "Ei verkkoyhteyttä. Tarkista, että puhelin on verkossa."

            is SocketTimeoutException -> "Yhteys aikakatkaistiin. Palvelin on hidas."

            is ClientRequestException -> "Hakuvirhe (${e.response.status.value}). Tarkista tiedot."

            is ServerResponseException -> "Palvelinvirhe (${e.response.status.value}). Yritä myöhemmin."

            is RedirectResponseException -> "Odottamaton uudelleenohjaus."

            is IOException -> "Verkkovirhe. Tarkista yhteys."

            else -> e.message ?: "Tuntematon virhe tapahtui: ${e.javaClass.simpleName}"
        }
        Result.Error(errorMessage)
    }
}