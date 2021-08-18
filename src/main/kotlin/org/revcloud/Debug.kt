package org.revcloud

import org.http4k.core.Filter

fun debug() = Filter { next ->
  { request ->
    next(request).also { response -> println("Response from Pokemon API: $response") }
  }
}
