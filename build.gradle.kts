plugins {
    java
    // https://github.com/johnrengelman/shadow
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

var ver : String? = rootProject.properties["version"] as String
group = "com.here.naksha"
if (ver != null) {
    version = ver as String
}

tasks {
    shadowJar {
        // Include the output from subprojects in the shadowJar task
        subprojects.forEach { subproject ->
            dependsOn(subproject.tasks.named<Jar>("jar"))
            from(subproject.tasks.named<Jar>("jar").map { it.archiveFile })
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
