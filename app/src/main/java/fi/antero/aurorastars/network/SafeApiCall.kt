package fi.antero.aurorastars.network

import fi.antero.aurorastars.util.Result
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse

suspend inline fun <reified T> safeApiCall(
    call: () -> HttpResponse
): Result<T> {
    return try {
        val response: HttpResponse = call()
        val data: T = response.body()
        Result.Success(data)
    } catch (e: Exception) {
        val message: String = e.message ?: "Verkkovirhe"
        Result.Error(message)
    }
}
