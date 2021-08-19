plugins {
  kotlin("jvm")
  application
}

application {
  mainClass.set("org.revcloud.Main")
}

repositories {
  mavenCentral()
}

java.sourceCompatibility = JavaVersion.VERSION_11

tasks {
  compileKotlin.get().kotlinOptions.jvmTarget = JavaVersion.VERSION_11.toString()
  compileTestKotlin.get().kotlinOptions.jvmTarget = JavaVersion.VERSION_11.toString()
  test.get().useJUnitPlatform()

  register<Zip>("buildZip") {
    from(compileKotlin)
    from(processResources)
    into("lib") {
      from(configurations.runtimeClasspath)
    }
  }
}

dependencies {
  val http4kVersion: String by project
  implementation("org.http4k:http4k-core:$http4kVersion")
  implementation("org.http4k:http4k-serverless-lambda:$http4kVersion")
  implementation("org.http4k:http4k-format-moshi:$http4kVersion")
  implementation("org.http4k:http4k-connect-amazon-dynamodb:3.6.3.1")
  testImplementation("org.http4k:http4k-testing-hamkrest:$http4kVersion")
  val junitVersion: String by project
  testImplementation(platform("org.junit:junit-bom:$junitVersion"))
  testImplementation("org.junit.jupiter:junit-jupiter-api")
  testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}
