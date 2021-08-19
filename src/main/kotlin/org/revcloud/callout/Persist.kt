package org.revcloud.callout

import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.routing.RoutingHttpHandler
import org.http4k.routing.bind
import org.revcloud.persistCalloutResponse

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
