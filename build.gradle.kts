import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.6.0"
}

group = "keine.ahnung"
version = "0.0.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.google.maps:google-maps-services:1.0.1")
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "1.8"
}