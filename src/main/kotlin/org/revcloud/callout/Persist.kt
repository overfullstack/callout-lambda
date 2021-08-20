package org.revcloud.callout

import dev.forkhandles.result4k.Result
import org.http4k.connect.RemoteFailure
import org.http4k.connect.amazon.dynamodb.action.ModifiedItem
import org.http4k.connect.amazon.dynamodb.model.Attribute
import org.http4k.connect.amazon.dynamodb.model.Item
import org.http4k.connect.amazon.dynamodb.model.TableName
import org.http4k.connect.amazon.dynamodb.putItem
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.routing.RoutingHttpHandler
import org.http4k.routing.bind
import org.revcloud.getAvalaraTableName
import org.revcloud.getDynamoDbClient
import java.util.UUID

fun persistCalloutResponse(
  avalaraClient: AvalaraClient
): RoutingHttpHandler = "/" bind Method.POST to { req: Request ->
  val calloutResponse = avalaraClient.response(req)
  val (uuid, _) = persistCalloutResponse(calloutResponse)
  Response(Status.OK)
    .header("uuid", uuid)
    .header("content-type", "application/json")
    .body(calloutResponse)
}

private val attrIdS = Attribute.string().required("Id")
private val attrLogS = Attribute.string().required("log")

fun persistCalloutResponse(
  calloutResponse: String
): Pair<String, Result<ModifiedItem, RemoteFailure>> {
  val dynamoDbClient = getDynamoDbClient()
  val uuid = UUID.randomUUID().toString()
  val dbResult = dynamoDbClient.putItem(
    TableName.of(getAvalaraTableName()),
    Item = Item(
      attrIdS of uuid,
      attrLogS of calloutResponse,
    )
  )
  println(dbResult)
  return uuid to dbResult
}
