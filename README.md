# xyz-hub-ext-template
Template for proprietary Naksha extensions.

To use this:
- First clone the repository. 
- Refer to the example handle classes in `src/` to know how to write a custom handler. 
- Install Gradle, the build tool used for this project.
- The `build.gradle.kts` file contains instructions for Gradle to build and export the fat JAR artifact. Run `gradle build`.
- Run `gradle shadowJar`.