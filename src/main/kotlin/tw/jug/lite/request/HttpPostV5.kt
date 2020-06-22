package tw.jug.lite.request

import io.ktor.client.HttpClient
import io.ktor.client.engine.apache.Apache
import io.ktor.client.features.json.GsonSerializer
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.request.post
import io.ktor.client.request.url
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import tw.jug.lite.model.Person

class HttpPostV5(val person: Person) {

    fun post(): HttpStatusCode {
        val client = HttpClient(Apache) {
            install(JsonFeature) {
                serializer = GsonSerializer {
                    serializeNulls()
                    disableHtmlEscaping()
                }
            }
        }

        return runBlocking {
            val response = async {
                client.post<HttpResponse>() {
                    contentType(ContentType.Application.Json)
                    url("http://127.0.0.1:5987/")
                    body = person
                }
            }
            response.await().status
        }
    }
}

fun main() {
    val post = HttpPostV5(Person("Jimin", 181))
    println(post.post())
}



