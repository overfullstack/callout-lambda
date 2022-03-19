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
    app(pokeCoApi = fakePokemonApi()).asServer(SunHttp(7001)).start()
    val appClient = JavaHttpClient()
    appClient(Request(GET, "http://localhost:7001/callout/pokemon"))
  }

  @Test
  fun avalara() {
    app(avalaraApi = fakeAvalaraApi()).asServer(SunHttp(8001)).start()
    val appClient = JavaHttpClient()
    appClient(Request(GET, "http://localhost:8001/callout/avalara"))
  }
}
