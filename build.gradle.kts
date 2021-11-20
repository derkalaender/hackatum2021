import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.6.0"
    kotlin("plugin.serialization") version "1.5.30"
    id("com.github.johnrengelman.shadow") version "7.0.0"
    application
}

group = "keine.ahnung"
version = "0.0.0"

repositories {
    mavenCentral()
}

val ktorVersion = "1.6.5"

dependencies {
    implementation("com.google.maps:google-maps-services:1.0.1")
    implementation("ch.qos.logback:logback-classic:1.3.0-alpha10")
    implementation("ch.qos.logback:logback-core:1.3.0-alpha10")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.2-native-mt")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.5.2-native-mt")

    implementation("org.jetbrains.kotlinx:kotlinx-html-jvm:0.7.3")

    implementation("io.ktor:ktor-server-core:$ktorVersion")
    implementation("io.ktor:ktor-server-netty:$ktorVersion")

    implementation("io.ktor:ktor-client-core:$ktorVersion")
    implementation("io.ktor:ktor-client-cio:$ktorVersion")
    implementation("io.ktor:ktor-client-json:$ktorVersion")
    implementation("io.ktor:ktor-client-serialization:$ktorVersion")

    implementation("io.ktor:ktor-http:$ktorVersion")
    implementation("io.ktor:ktor-html-builder:$ktorVersion")
    implementation("io.ktor:ktor-serialization:$ktorVersion")

    implementation("io.ktor:ktor-network-tls-certificates:$ktorVersion")
    implementation("io.ktor:ktor-auth:$ktorVersion")

    implementation("org.jetbrains.kotlin:kotlin-reflect:1.5.30")

    implementation("io.github.microutils:kotlin-logging:2.0.11")
    implementation("org.slf4j:slf4j-api:2.0.0-alpha4")
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "1.8"
    kotlinOptions.freeCompilerArgs += "-Xopt-in=kotlin.RequiresOptIn"
}