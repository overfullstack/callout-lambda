package org.revcloud

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
import org.http4k.routing.header

fun findAllWithPrefix(
  client: Client
): RoutingHttpHandler = "/{prefix}" bind Method.GET to { req: Request ->
  val prefixLens = Path.of("prefix")
  val prefix = prefixLens(req)
  val results = client.list()
  val pokemonList = results.map { it.name }.filter { it.startsWith(prefix) }
  
  val (uuid, dbResult) = insert(prefix, pokemonList)
  val resultLens = Body.auto<LambdaResponse>().toLens()
  Response(Status.OK)
    .header("uuid", uuid)
    .with(resultLens of LambdaResponse(pokemonList, dbResult.failureOrNull()))
}

data class LambdaResponse(val pokemon: List<String>, val failure: RemoteFailure?)
