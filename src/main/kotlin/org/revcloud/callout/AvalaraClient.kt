package org.revcloud.callout

import org.http4k.core.HttpHandler
import org.http4k.core.Method
import org.http4k.core.Request

class AvalaraClient(private val calloutApi: HttpHandler) {
  fun response(req: Request): String {
    val response = calloutApi(
      Request(
        Method.POST,
        "/api/v2/transactions/create?\$include=Lines,Details,Summary,Addresses,TaxDetailsByTaxType"
      )
        .header("Authorization", req.header("Authorization") ?: "Basic bWdhbmFwYXRoeUBzdGVlbGJyaWNrLmNvbTpCaWxsaW5nJDEyMw==")
        .body(req.body)
    )
    return response.body.toString()
  }
}
