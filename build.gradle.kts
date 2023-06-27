import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

plugins {
    java
    `java-library`
    `maven-publish`
    // https://github.com/diffplug/spotless
    // gradle spotlessApply
    id("com.diffplug.spotless").version("6.11.0")
    // https://github.com/johnrengelman/shadow
    id("com.github.johnrengelman.shadow") version "7.1.2"
    // Don't apply for all projects, we individually only apply where Kotlin is used.
    kotlin("jvm") version "1.8.22"
}

group = "com.here.naksha"
version = rootProject.properties["version"] as String

val mavenUrl = rootProject.properties["mavenUrl"] as String
val mavenUser = rootProject.properties["mavenUser"] as String
val mavenPassword = rootProject.properties["mavenPassword"] as String

dependencies {
    implementation("org.jetbrains:annotations:24.0.1")
    implementation("com.here.naksha:here-naksha-lib-extension:2.0.3")
    implementation(kotlin("stdlib-jdk8"))
}

repositories {
    maven(uri("https://repo.osgeo.org/repository/release/"))
    maven(uri("https://artifactory.in.here.com/artifactory/cme-content-tools-maven-release"))
    mavenCentral()
}

// https://github.com/diffplug/spotless/tree/main/plugin-gradle
spotless {
    java {
        encoding("UTF-8")
        val YEAR = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy"))
        licenseHeader(
            """
/*
 * Copyright (C) 2017-$YEAR HERE Europe B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * SPDX-License-Identifier: Apache-2.0
 * License-Filename: LICENSE
 */
"""
        )
        // Allow "spotless:off" / "spotless:on" comments to toggle spotless auto-format.
        toggleOffOn()
        removeUnusedImports()
        importOrder()
        formatAnnotations()
        palantirJavaFormat()
        indentWithTabs(4)
        indentWithSpaces(2)
    }
}

tasks {
    test {
        useJUnitPlatform()
    }

    compileJava {
        finalizedBy(spotlessApply)
    }
    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions.jvmTarget = JavaVersion.VERSION_17.toString()
        finalizedBy(spotlessApply)
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}