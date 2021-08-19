@file:Suppress("unused")

package org.revcloud

import org.http4k.core.HttpHandler
import org.http4k.core.then
import org.http4k.routing.bind
import org.http4k.routing.routes
import org.http4k.serverless.ApiGatewayV1LambdaFunction
import org.revcloud.callout.CalloutClient
import org.revcloud.callout.persistCalloutResponse

class Lambda : ApiGatewayV1LambdaFunction(app())

fun app(pokeCoApi: HttpHandler = pokemonApi(), calloutApi: HttpHandler = fakeCalloutApi()): HttpHandler {
  return debug().then(
    "/callout" bind routes(
      "/real" bind findAllWithPrefix(PokemonClient(pokeCoApi)),
      "/fake" bind persistCalloutResponse(CalloutClient(calloutApi))
    )
  )
}
