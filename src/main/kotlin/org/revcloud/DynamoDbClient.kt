package org.revcloud

import org.http4k.aws.AwsCredentials
import org.http4k.client.JavaHttpClient
import org.http4k.connect.amazon.core.model.Region
import org.http4k.connect.amazon.dynamodb.DynamoDb
import org.http4k.connect.amazon.dynamodb.Http
import org.http4k.core.Uri
import org.http4k.core.then
import org.http4k.filter.ClientFilters

private val isRunningOnCloud = System.getenv()["AWS_LAMBDA_FUNCTION_NAME"] != null
fun getDynamoDbClient() = if (isRunningOnCloud) {
  DynamoDb.Http()
} else {
  DynamoDb.Http(
    Region.of("ap-south-1"),
    { AwsCredentials("accessKeyId", "secretKey") },
    ClientFilters.SetBaseUriFrom(Uri.of("http://localhost:8000")).then(JavaHttpClient())
  )
}

fun getAvalaraTableName() = if (isRunningOnCloud) System.getenv()["AVALARA_TABLE"] ?: "Avalara" else "Avalara"
fun getPokemonTableName() = if (isRunningOnCloud) System.getenv()["POKEMON_TABLE"] ?: "Pokemon" else "Pokemon"
