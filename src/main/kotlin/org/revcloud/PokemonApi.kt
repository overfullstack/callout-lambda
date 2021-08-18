package org.revcloud

import org.http4k.client.JavaHttpClient
import org.http4k.core.Uri
import org.http4k.core.then
import org.http4k.filter.ClientFilters
import org.http4k.filter.ClientFilters.SetBaseUriFrom
import org.http4k.filter.ResponseFilters

/**
 * We've added a reporting filter here so we can determine Serverless performance
 * without the remote latency.
 */
fun pokemonApi() =
  ResponseFilters.ReportHttpTransaction {
    println("Pokemon API took " + it.duration.toMillis() + "ms")
  }
    .then(SetBaseUriFrom(Uri.of("https://pokeapi.co")))
    .then(JavaHttpClient())
