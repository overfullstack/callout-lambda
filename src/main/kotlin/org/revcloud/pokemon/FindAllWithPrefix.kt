package org.revcloud.pokemon

import dev.forkhandles.result4k.Result
import dev.forkhandles.result4k.failureOrNull
import org.http4k.connect.RemoteFailure
import org.http4k.connect.amazon.dynamodb.action.ModifiedItem
import org.http4k.connect.amazon.dynamodb.model.Attribute
import org.http4k.connect.amazon.dynamodb.model.AttributeValue
import org.http4k.connect.amazon.dynamodb.model.Item
import org.http4k.connect.amazon.dynamodb.model.TableName
import org.http4k.connect.amazon.dynamodb.model.with
import org.http4k.connect.amazon.dynamodb.putItem
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
import org.revcloud.getDynamoDbClient
import org.revcloud.getPokemonTableName
import java.util.UUID

fun findAllWithPrefix(
  pokemonClient: PokemonClient
): RoutingHttpHandler = "/{prefix}" bind Method.GET to { req: Request ->
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

private val attrIdS = Attribute.string().required("Id")
private val attrS = Attribute.string().required("Prefix")
private val attrN = Attribute.int().required("Count")
private val attrM = Attribute.map().required("Response")
private val attrL = Attribute.list().required("Pokemon")
fun insertPokemon(
  prefix: String,
  pokemonList: List<String>
): Pair<String, Result<ModifiedItem, RemoteFailure>> {
  val dynamoDbClient = getDynamoDbClient()
  val uuid = UUID.randomUUID().toString()
  val pokemon = pokemonList.map { AttributeValue.Str(it) }
  val dbResult = dynamoDbClient.putItem(
    TableName.of(getPokemonTableName()),
    Item = Item(
      attrIdS of uuid,
      attrN of pokemonList.size,
      attrS of prefix,
      attrM of Item().with(attrL of pokemon),
    )
  )
  println(dbResult)
  return uuid to dbResult
}
