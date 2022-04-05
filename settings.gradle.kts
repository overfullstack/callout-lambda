pluginManagement {
  repositories {
    gradlePluginPortal() // This is for other community plugins
  }
  val kotlinVersion: String by sett1.6.20
  plugins {
    kotlin("jvm") version kotlinVersion
  }
}

rootProject.name = "callout-lambda"
