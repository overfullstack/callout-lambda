package org.revcloud.callout

import org.http4k.client.JavaHttpClient
import org.http4k.core.Uri
import org.http4k.core.then
import org.http4k.filter.ClientFilters
import org.http4k.filter.ResponseFilters

private val isRunningOnCloud = System.getenv()["AWS_LAMBDA_FUNCTION_NAME"] != null

fun avalaraApi() =
  ResponseFilters.ReportHttpTransaction {
    println("Avalara API took ${it.duration.toMillis()}ms")
  }
    .then(ClientFilters.SetBaseUriFrom(Uri.of("https://sandbox-rest.avatax.com")))
    .then(JavaHttpClient())
