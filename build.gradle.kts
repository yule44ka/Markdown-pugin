plugins {
    kotlin("jvm") version "1.9.22"
    application
}

group = "com.example"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.9.10") // Optional for reflection
}

application {
    mainClass.set("MainKt")
}
