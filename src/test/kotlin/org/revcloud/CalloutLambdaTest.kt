package org.revcloud

import org.http4k.client.JavaHttpClient
import org.http4k.core.Method.GET
import org.http4k.core.Request
import org.http4k.server.SunHttp
import org.http4k.server.asServer
import org.junit.jupiter.api.Test

class CalloutLambdaTest {
  @Test
  fun pokemon() {
    val http = fakePokemonApi()
    app(pokeCoApi = http).asServer(SunHttp(7001)).start()
    val appClient = JavaHttpClient()
    appClient(Request(GET, "http://localhost:7001/callout/pokemon"))
  }

  @Test
  fun avalara() {
    val http = fakeAvalaraApi()
    app(avalaraApi = http).asServer(SunHttp(7001)).start()
    val appClient = JavaHttpClient()
    appClient(Request(GET, "http://localhost:7001/callout/avalara"))
  }
}
