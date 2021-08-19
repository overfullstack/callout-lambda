package org.revcloud

import dev.forkhandles.result4k.Result
import org.http4k.aws.AwsCredentials
import org.http4k.client.JavaHttpClient
import org.http4k.connect.RemoteFailure
import org.http4k.connect.amazon.core.model.Region
import org.http4k.connect.amazon.dynamodb.DynamoDb
import org.http4k.connect.amazon.dynamodb.Http
import org.http4k.connect.amazon.dynamodb.action.ModifiedItem
import org.http4k.connect.amazon.dynamodb.model.Attribute
import org.http4k.connect.amazon.dynamodb.model.AttributeValue
import org.http4k.connect.amazon.dynamodb.model.Item
import org.http4k.connect.amazon.dynamodb.model.TableName
import org.http4k.connect.amazon.dynamodb.model.with
import org.http4k.connect.amazon.dynamodb.putItem
import org.http4k.core.Uri
import org.http4k.core.then
import org.http4k.filter.ClientFilters
import java.util.UUID

private val attrIdS = Attribute.string().required("Id")
private val attrS = Attribute.string().required("Prefix")
private val attrN = Attribute.int().required("Count")
private val attrM = Attribute.map().required("Response")
private val attrL = Attribute.list().required("Pokemon")
private val attrCalloutS = Attribute.string().required("log")

private val isRunningOnCloud = System.getenv()["AWS_LAMBDA_FUNCTION_NAME"] != null

fun insertPokemon(
  prefix: String,
  pokemonList: List<String>
): Pair<String, Result<ModifiedItem, RemoteFailure>> {
  val dynamoDbClient = getDynamoDbClient()
  val uuid = UUID.randomUUID().toString()
  val pokemon = pokemonList.map { AttributeValue.Str(it) }
  val dbResult = dynamoDbClient.putItem(
    TableName.of("Pokemon"), Item = Item(
      attrIdS of uuid,
      attrN of pokemonList.size,
      attrS of prefix,
      attrM of Item().with(attrL of pokemon),
    )
  )
  println(dbResult)
  return uuid to dbResult
}

fun persistCalloutResponse(
  calloutResponse: String
): Pair<String, Result<ModifiedItem, RemoteFailure>> {
  val dynamoDbClient = getDynamoDbClient()
  val uuid = UUID.randomUUID().toString()
  val dbResult = dynamoDbClient.putItem(
    TableName.of("CalloutLog"), Item = Item(
      attrIdS of uuid,
      attrCalloutS of calloutResponse,
    )
  )
  println(dbResult)
  return uuid to dbResult
}

private fun getDynamoDbClient() = if (isRunningOnCloud) {
  DynamoDb.Http()
} else {
  DynamoDb.Http(
    Region.of("ap-south-1"),
    { AwsCredentials("accessKeyId", "secretKey") },
    ClientFilters.SetBaseUriFrom(Uri.of("http://localhost:8000")).then(JavaHttpClient())
  )
}
