@file:JvmName("Main")
package org.revcloud

import org.http4k.server.SunHttp
import org.http4k.server.asServer

/**
 * Only for local testing. AWS uses Pokemon4kLambda
 */
fun main() {
  pokemon4k().asServer(SunHttp(7000)).start()
}
