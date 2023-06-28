plugins {
    java
    // https://github.com/johnrengelman/shadow
    id("com.github.johnrengelman.shadow") version "7.1.2"
    id("com.tomgregory.project-order") version "1.0.0"
}

var ver : String? = rootProject.properties["version"] as String
group = "com.here.naksha"
if (ver != null) {
    version = ver as String
}

tasks {
    shadowJar {
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
}