tasks.wrapper {
    gradleVersion = "8.1.1"
    distributionType = Wrapper.DistributionType.ALL
}

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
            val title : String? = rootProject.properties["title"] as String?
            if (title == null) {
                attributes["Implementation-Title"] = "Naksha Extension"
            } else {
                attributes["Implementation-Title"] = title
            }
            attributes["Main-Class"] = "com.here.naksha.lib.extension.Main"
        }
    }
}