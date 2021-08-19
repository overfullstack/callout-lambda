package org.revcloud.pokemon

import dev.forkhandles.result4k.failureOrNull
import org.http4k.connect.RemoteFailure
import org.http4k.core.Body
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.core.with
import org.http4k.format.Moshi.auto
import org.http4k.lens.Path
import org.http4k.routing.RoutingHttpHandler
import org.http4k.routing.bind
import org.revcloud.insertPokemon

fun findAllWithPrefix(
  pokemonClient: PokemonClient
): RoutingHttpHandler = "/{prefix}" bind Method.POST to { req: Request ->
  val prefixLens = Path.of("prefix")
  val prefix = prefixLens(req)
  val results = pokemonClient.list()
  val pokemonList = results.map { it.name }.filter { it.startsWith(prefix) }

  val (uuid, dbResult) = insertPokemon(prefix, pokemonList)
  val resultLens = Body.auto<PokemonLambdaResponse>().toLens()
  Response(Status.OK)
    .header("uuid", uuid)
    .with(resultLens of PokemonLambdaResponse(pokemonList, dbResult.failureOrNull()))
}

data class PokemonLambdaResponse(val pokemon: List<String>, val failure: RemoteFailure?)
