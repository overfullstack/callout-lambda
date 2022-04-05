package org.revcloud

import org.http4k.client.JavaHttpClient
import org.http4k.core.HttpHandler
import org.http4k.core.Method.GET
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.server.SunHttp
import org.http4k.server.asServer
import org.junit.jupiter.api.Test
import java.io.File

private const val port = 8001

class CalloutLambdaTest {

  @Test
  fun avalara() {
    app(avalaraApi = fakeAvalaraApi()).asServer(SunHttp(port)).start()
    val appClient = JavaHttpClient()
    appClient(Request(GET, "http://localhost:$port/callout/avalara"))
  }
}

fun fakeAvalaraApi(): HttpHandler = { _: Request ->
  val fakeResponse = readFileFromTestResource("fake-avalara-response.json")
  Response(Status(200, ""))
    .body(fakeResponse)
}

private fun readFileFromTestResource(fileRelativePath: String): String =
  File("src/test/resources/$fileRelativePath").readText()
