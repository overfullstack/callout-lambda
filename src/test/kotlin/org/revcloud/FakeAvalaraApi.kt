package org.revcloud

import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status
import java.io.File

fun fakeAvalaraApi() = { req: Request ->
  val fakeResponse = readFileFromTestResource("fake-avalara-response.json")
  Response(Status(200, ""))
    .body(fakeResponse)
}

private fun readFileFromTestResource(fileRelativePath: String): String =
  File("src/test/resources/$fileRelativePath").readText()
