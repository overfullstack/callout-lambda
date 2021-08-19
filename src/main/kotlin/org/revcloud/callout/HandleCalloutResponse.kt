package org.revcloud.callout

import dev.forkhandles.result4k.failureOrNull
import org.http4k.connect.RemoteFailure
import org.http4k.core.Body
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.core.with
import org.http4k.format.Moshi.auto
import org.http4k.routing.RoutingHttpHandler
import org.http4k.routing.bind
import org.revcloud.persistCalloutResponse

fun persistCalloutResponse(
  calloutClient: CalloutClient
): RoutingHttpHandler = "/" bind Method.POST to { req: Request ->
  val calloutResponse = calloutClient.response()
  val (uuid, _) = persistCalloutResponse(calloutResponse)
  Response(Status.OK)
    .header("uuid", uuid)
    .body(calloutResponse)
}

