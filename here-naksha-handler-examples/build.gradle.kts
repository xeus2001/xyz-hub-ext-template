import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

plugins {
    // https://github.com/diffplug/spotless
    id("com.diffplug.spotless").version("6.11.0")
    kotlin("jvm") version "1.8.22"
}

group = "com.here.naksha"
version = rootProject.version

apply {
    plugin("java")
}

// This how to read properties.
val mavenUrl = (rootProject.properties["mavenUrl"] as String?)?.trim()
val mavenUser = (rootProject.properties["mavenUser"] as String?)?.trim()
val mavenPassword = (rootProject.properties["mavenPassword"] as String?)?.trim()

if (mavenUrl != null) {
    println("Add maven repository: "+mavenUrl)
    repositories {
        maven(uri(mavenUrl))
        mavenCentral()
    }
}

dependencies {
    // Necessary for the handler to implement IEventHandler
    implementation("org.jetbrains:annotations:24.0.1")
    implementation("com.here.naksha:here-naksha-lib-core:2.0.3")
    implementation("com.here.naksha:here-naksha-lib-extension:2.0.3")
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
    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions.jvmTarget = JavaVersion.VERSION_17.toString()
        finalizedBy(spotlessApply)
    }
    compileJava {
        finalizedBy(spotlessApply)
    }
}
