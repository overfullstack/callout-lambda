package org.revcloud

import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status
import java.io.File

private val isRunningOnCloud = System.getenv()["AWS_LAMBDA_FUNCTION_NAME"] != null

fun fakeCalloutApi() = { req: Request ->
  val fakeResponse = readFileFromMainResource("fake-callout-response.json")
  Response(Status(200, ""))
    .body(fakeResponse)
}

fun readFileFromMainResource(fileRelativePath: String): String = File(
  if (isRunningOnCloud) fileRelativePath else "src/main/resources/$fileRelativePath"
).readText()
