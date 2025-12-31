package fi.antero.aurorastars.network

import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.accept
import io.ktor.http.ContentType
import io.ktor.serialization.kotlinx.json.json // TÄRKEÄ MUUTOS: json gsonin tilalle
import kotlinx.serialization.json.Json

object NetworkClient {
    val httpClient = HttpClient(Android) {
        install(ContentNegotiation) {

            json(Json {
                ignoreUnknownKeys = true
                isLenient = true
            })
        }

        defaultRequest {
            accept(ContentType.Application.Json)
        }
    }
}