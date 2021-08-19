package org.revcloud.callout

import org.http4k.core.HttpHandler
import org.http4k.core.Method
import org.http4k.core.Request

class CalloutClient(private val calloutApi: HttpHandler) {
  fun response(): String {
    val response = calloutApi(Request(Method.POST, "tbd"))
    return response.body.toString()
  }
}
