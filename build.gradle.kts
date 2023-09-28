val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project

plugins {
    kotlin("jvm") version "1.8.21"
    id("io.ktor.plugin") version "2.2.4"
    id("com.android.application") version "7.1.2" apply false
    id("com.android.library") version "7.1.2" apply false
    id("org.jetbrains.kotlin.android") version "1.8.21" apply false
    id("com.google.gms.google-services") version "4.3.15" apply false
    id("org.jetbrains.kotlin.plugin.serialization") version "1.8.20"
}


group = "com.twister.topic"
version = "0.0.1"
application {
    mainClass.set("com.twister.topic.ApplicationKt")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

buildscript {

    repositories {
        google()  // Google's Maven repository
        mavenCentral()  // Maven Central repository
    }

    dependencies {
        classpath("com.google.gms:google-services:4.3.15")
        classpath("com.android.tools.build:gradle:2.1.0")
        classpath("org.jetbrains.kotlin:kotlin-serialization:1.8.20")
    }
}

apply(plugin = "org.jetbrains.kotlin.plugin.serialization")

dependencies {
    implementation("io.ktor:ktor-server-core-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-netty-jvm:$ktor_version")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    implementation("com.google.firebase:firebase-bom:32.0.0")
    implementation("com.google.firebase:firebase-analytics:17.5.0")
    implementation("com.google.firebase:firebase-firestore-ktx:24.5.0")
    implementation("com.google.firebase:firebase-admin:9.1.1"){
        exclude(group = "org.slf4j", module = "slf4j-api")
    }
    implementation("io.ktor:ktor-server-content-negotiation:$ktor_version")
    implementation("io.ktor:ktor-serialization-kotlinx-protobuf:$ktor_version")
    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktor_version")
    implementation("ch.qos.logback:logback-classic:1.2.11")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.5.0")
    testImplementation("io.ktor:ktor-server-tests-jvm:$ktor_version")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
    testImplementation("org.mockito.kotlin:mockito-kotlin:3.2.0")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.1")
}