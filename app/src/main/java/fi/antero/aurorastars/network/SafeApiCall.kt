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
        val errorCode = when (e) {
            is UnknownHostException -> "NETWORK_NO_CONNECTION"
            is SocketTimeoutException -> "NETWORK_TIMEOUT"
            is ClientRequestException -> "API_CLIENT_ERROR_${e.response.status.value}"
            is ServerResponseException -> "API_SERVER_ERROR_${e.response.status.value}"
            is RedirectResponseException -> "API_REDIRECT"
            is IOException -> "NETWORK_IO_ERROR"
            else -> "NETWORK_UNKNOWN_ERROR"
        }
        Result.Error(errorCode)
    }
}