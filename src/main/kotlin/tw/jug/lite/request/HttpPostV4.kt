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
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import tw.jug.lite.model.Person

/**
 * fix java.lang.IllegalStateException: Fail to send body
 */
class HttpPostV4(val person: Person) {

    fun post(): HttpStatusCode {
        val client = HttpClient(Apache) {
            install(JsonFeature) {
                serializer = GsonSerializer {
                    serializeNulls()
                    disableHtmlEscaping()
                }
            }
        }

        val response = GlobalScope.async {
            client.post<HttpResponse>() {
                // https://github.com/ktorio/ktor/issues/1794
                contentType(ContentType.Application.Json)
                url("http://127.0.0.1:5987/")
                body = person
            }
        }

        return runBlocking {
            response.await().status
        }
    }

}

fun main() {
    val post = HttpPostV4(Person("Jimin", 181))
    println(post.post())
}