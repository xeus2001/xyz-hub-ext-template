# Naksha Extension Template

[img_purpose]: diagrams/purpose.png

This is Template repository for proprietary Naksha extensions.

## Purpose

![Purpose][img_purpose]

**What is Extension ?**

* Extension is a detached API module that can be developed/maintained in isolation to core XYZ Service.
* Extensions are not expected to process external API calls directly. Main XYZ Service communicates with Extensions using internal API calls.

**Why is it required ?**

* Extensions are required to attach **custom API processing** logic without modifying main XYZ Service.
* There can also be need to support **custom Data Storage**, outside of what is supported by main XYZ Service.


## How to create Extension

### 1. Create Repository

To create extension, you should first fork the repository with new name following convention - `<team>-ext-<purpose>`:
* Where,
  * `<team>` = Name of the team owning the extension, e.g. `naksha` (default), `dcu` ...
  * `<purpose>` = Uniquely define the purpose of extension, e.g. `validation`, `dcu-storage` ...
* For example,
  * `naksha-ext-validation`
  * `dcu-ext-dlb-storage`


### 2. Add Custom logic

Review the example handle classes in module 
`here-naksha-handler-examples` to know how to write a custom handler. Then create your own new 
module for your own handler code.

TODO : 
* Add examples of custom handler (one without DB storage, one with DB storage)




### 3. Add JUnits

TODO : 
* Add examples of JUnits



### 4. Compile and Build

To create a shadow jar:

```bash
# Use system gradle version to create a wrapper with the correct gradle version.
gradle wrapper
# Create a shadow JAR.
./gradlew shadowJar
# Run the extension using for example extension number 2323:
java -jar build/libs/naksha-extension-all.jar 2323
```

**Maven Repository**

If you need to add a specific company maven repository to be checked before the maven central, 
create a `gradle.properties` file in your home directory and add the URL and the title:

```bash
touch ~/.gradle/gradle.properties
echo mavenUrl="ADD-YOUR-MAVEN-URL" >> ~/.gradle/gradle.properties
echo title="ADD-SOME-PROJECT-TITLE" >> ~/.gradle/gradle.properties
```


### 5. Run on local

TODO :
* Steps to run XYZ Service on local
* Steps to run XYZ Extension on local
* Steps to configure Connector, Space in XYZ Service
* Steps to validate end-to-end REST API behaviour
