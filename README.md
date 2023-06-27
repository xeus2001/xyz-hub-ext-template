# xyz-hub-ext-template
Template for proprietary Naksha extensions.

To use this:
- First clone the repository. 
- Refer to the example handle classes in module `here-naksha-handler-examples` to know how to write a custom handler.
Create your own new module for your own handler code.
- Install Gradle, the build tool used for this project.
- The `build.gradle.kts` file contains instructions for Gradle to build and export the fat JAR artifact, so no further configuration for Gradle is required. Run simply `gradle build`.
- Run `gradle shadowJar`.