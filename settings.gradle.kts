pluginManagement {
  repositories {
    gradlePluginPortal() // This is for other community plugins
  }
  val kotlinVersion: String by sett1.6.21
  plugins {
    kotlin("jvm") version kotlinVersion
  }
}

rootProject.name = "callout-lambda"
