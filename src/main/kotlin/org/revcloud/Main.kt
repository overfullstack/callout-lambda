@file:JvmName("Main")
package org.revcloud

import org.http4k.server.SunHttp
import org.http4k.server.asServer

/**
 * Only for local testing. AWS uses Lambda
 */
fun main() {
  app().asServer(SunHttp(7000)).start()
}
