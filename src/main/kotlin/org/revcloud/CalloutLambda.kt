@file:Suppress("unused")

package org.revcloud

import org.http4k.core.HttpHandler
import org.http4k.core.then
import org.http4k.routing.bind
import org.http4k.routing.routes
import org.http4k.serverless.ApiGatewayV1LambdaFunction
import org.revcloud.callout.AvalaraClient
import org.revcloud.callout.avalaraApi
import org.revcloud.callout.persistCalloutResponse
import org.revcloud.pokemon.PokemonClient
import org.revcloud.pokemon.findAllWithPrefix
import org.revcloud.pokemon.pokemonApi

class CalloutLambda : ApiGatewayV1LambdaFunction(app())

fun app(
  pokemonApi: HttpHandler = pokemonApi(),
  avalaraApi: HttpHandler = avalaraApi()
): HttpHandler {
  return debug().then(
    "/callout" bind routes(
      "/pokemon" bind findAllWithPrefix(PokemonClient(pokemonApi)),
      "/avalara" bind persistCalloutResponse(AvalaraClient(avalaraApi))
    )
  )
}
