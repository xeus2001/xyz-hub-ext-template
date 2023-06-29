plugins {
    java
    // https://github.com/johnrengelman/shadow
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

var ver : String? = rootProject.properties["version"] as String
group = "com.here.naksha"
if (ver != null) {
    version = ver as String
}

subprojects {
    apply {
        plugin("java")
    }
    repositories {
        // Without this, the subprojects might try to find gradle plugins before they
        // inherit the repos from the root project
        maven(uri("https://artifactory.in.here.com/artifactory/cme-content-tools-maven-release"))
        mavenCentral()
    }
    dependencies {
        // Necessary for the handler to implement IEventHandler
        implementation("org.jetbrains:annotations:24.0.1")
        implementation("com.here.naksha:here-naksha-lib-core:2.0.3")
    }
}

dependencies {
    // Necessary for the shadowJar to include the Main class
    implementation("com.here.naksha:here-naksha-lib-extension:2.0.3")
}

repositories {
    maven(uri("https://artifactory.in.here.com/artifactory/cme-content-tools-maven-release"))
    mavenCentral()
}

tasks {
    shadowJar {
        subprojects.forEach { subproject ->
            dependsOn(subproject.tasks.named<Jar>("jar"))
            from(subproject.tasks.named<Jar>("jar").map { it.archiveFile })
        }
        subprojects.forEach {
            configurations.add(it.configurations.runtimeClasspath.get())
        }
        archiveClassifier.set("all")
        mergeServiceFiles()
        isZip64 = true
        manifest {
            attributes["Implementation-Title"] = "Naksha Remote Extension"
            attributes["Main-Class"] = "com.here.naksha.lib.extension.Main"
        }
    }
    wrapper {
        // Using the wrapper ensure compatibility with plugins, e.g. the shadowJar
        version = "8.1.1"
    }
}