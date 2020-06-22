package tw.jug.lite.request

import io.ktor.client.HttpClient
import io.ktor.client.engine.apache.Apache
import io.ktor.client.features.json.GsonSerializer
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.request.post
import io.ktor.client.request.url
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode
import tw.jug.lite.model.Person

class HttpPostV1(val person: Person) {

    //https://ktor.io/clients/http-client/quick-start/requests.html#specifying-a-body-for-requests
    fun post(): HttpStatusCode {
        val client = HttpClient(Apache) {
            install(JsonFeature) {
                serializer = GsonSerializer {
                    serializeNulls()
                    disableHtmlEscaping()
                }
            }
        }

        val response = client.post<HttpResponse>() {
            url("http://127.0.0.1:5987/")
            body = person
        }
        return response.status
    }
}

fun main() {

}
