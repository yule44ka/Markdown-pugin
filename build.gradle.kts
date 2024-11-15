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
    implementation("com.formdev:flatlaf:3.1") // FlatLaf for dark theme
    implementation("org.commonmark:commonmark:0.21.0") // CommonMark for Markdown parsing
}

application {
    mainClass.set("MainKt")
}
