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
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.9.10")
    implementation("com.formdev:flatlaf:3.1") // FlatLaf for dark theme
}

application {
    mainClass.set("MainKt")
}
