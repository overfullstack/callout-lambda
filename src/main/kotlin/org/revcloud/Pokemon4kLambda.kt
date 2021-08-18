@file:Suppress("unused")

package org.revcloud

import org.http4k.core.HttpHandler
import org.http4k.core.then
import org.http4k.routing.routes
import org.http4k.server.SunHttp
import org.http4k.server.asServer
import org.http4k.serverless.ApiGatewayV1LambdaFunction

class Pokemon4kLambda : ApiGatewayV1LambdaFunction(pokemon4k())

fun pokemon4k(pokeCoApi: HttpHandler = pokemonApi()): HttpHandler {
  val client = Client(pokeCoApi)
  return debug().then(
    routes(findAllWithPrefix(client))
  )
}
