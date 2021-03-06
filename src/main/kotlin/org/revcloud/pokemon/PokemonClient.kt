package org.revcloud.pokemon

import org.http4k.core.Body
import org.http4k.core.HttpHandler
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.format.Moshi.auto

/**
 * A HTTP client for the Pokemon API.
 */
class PokemonClient(private val pokemonApi: HttpHandler) {
  private val body = Body.auto<Results>().toLens()
  fun list(): List<Pokemon> {
    val response: Response = pokemonApi(Request(Method.GET, "/api/v2/pokemon?limit=100"))
    return body(response).results
  }
}

data class Pokemon(val name: String)

data class Results(val results: List<Pokemon>)
