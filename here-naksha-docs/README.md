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

To create extension, you should first fork the repository. You can also change the name of your
forked repo, but more importantly it is advisable to define a new name for the jar artifact, and the
pakage that will contain your custom handler code, following this convention - `<team>-ext-<purpose>`:
* Where,
  * `<team>` = Name of the team owning the extension, e.g. `naksha` (default), `dcu` ...
  * `<purpose>` = Uniquely define the purpose of extension, e.g. `validation`, `dcu-storage` ...
* For example,
  * `naksha-ext-validation`
  * `dcu-ext-dlb-storage`

To change the title of the jar, create (if not exist) the file `gradle.properties` at the root directory of this project.
Add the definition `title=NEW-NAME-HERE` in a new line, with the name itself unquoted.

Similarly, create a new subproject with the same naming convention above. This should be where you put your code later.

### 2. Add dependencies
The Naksha artifacts are available in the HERE Artifactory in [cme-content-tools-maven-release](https://artifactory.in.here.com/ui/repos/tree/General/cme-content-tools-maven-release/com/here/naksha/).
To be able to use the Naksha you need to add the HERE Artifactory to:

pom.xml
```xml
<metadata>
  <groupId>com.here.naksha</groupId>
  <artifactId>here-naksha-lib-core</artifactId>
  <versioning>
    <latest>{$VERSION}</latest>
    <release>{$VERSION}</release>
    <versions>
      <version>{$VERSION}</version>
    </versions>
  </versioning>
</metadata> 
````

build.gradle.kts
```gradle
repositories {
  mavenLocal()
  maven(uri("https://artifactory.in.here.com/artifactory/cme-content-tools-maven-release"))
  mavenCentral()
}
dependencies {
  implementation("com.here.naksha:here-naksha-lib-core:{$VERSION}")
}
```

### 3. Add Custom logic

Review the example handle classes in module 
`here-naksha-handler-examples` to know how to write a custom handler. Then create your own new 
module for your own handler code. Like mentioned above, notice the package name in the example. Your package name should reflect important information about 
your team and the usage, like in the naming convention above.

#### 1. Custom handler example with Modify Features Event request.
Below is the basic handler example implemented by using Naksha artifacts, We have used ModifyFeaturesEvent for an example.
```java
public class ValidationHandler implements IEventHandler {

  public ValidationHandler(@NotNull Connector connector) throws XyzErrorException {
  }

  public @NotNull
  XyzResponse processEvent(@NotNull IEventContext eventContext) throws XyzErrorException {
    final Event event = eventContext.getEvent();
    if (event instanceof ModifyFeaturesEvent mfe) {
      final List<Feature> insertFeatures = mfe.getInsertFeatures();
      //mfe.getUpdateFeatures();
      //mfe.getUpsertFeatures();
      //mfe.getDeleteFeatures();
      if (insertFeatures != null) {
        for (final Feature feature : insertFeatures) {
          //Add your logic here
        }
      }
    }
    //Return your response
    //return eventContext.sendUpstream(event);    
    return new SuccessResponse()
            .withStatus(String.format("Example success response for event with stream ID %s", event.getStreamId()));
  }
}
```

#### 2. Custom handler example with Database storage.
Below is the basic code to use database configuration in your handler, You might need to add psql dependencies in your repository to use below DataBase code.
```java
public class ExamplePsqlHandler implements IEventHandler {
  public ExamplePsqlHandler(@NotNull Connector connector) throws XyzErrorException {
    sqlConfig = new PsqlConfigBuilder()
            .withAppName("testApp")
            .withSchema("testSchema")
            .withDb("testDB")
            .withHost("testHost")
            .withPort(5353)
            .build();
  }
  final PsqlConfig sqlConfig;

  @Override
  public @NotNull XyzResponse processEvent(@NotNull IEventContext eventContext) throws XyzErrorException {
    final Event event = eventContext.getEvent();
    //You case use sqlConfig and add DB logic
    logger.info("sqlConfig App name is :{} ", sqlConfig.appName);
    if (event instanceof ModifyFeaturesEvent mfe) {
      final List<Feature> insertFeatures = mfe.getInsertFeatures();
      //mfe.getUpdateFeatures();
      //mfe.getUpsertFeatures();
      //mfe.getDeleteFeatures();
      //eventContext.sendUpstream(new GetFeaturesByBBoxEvent());
      if (insertFeatures != null) {
        for (final Feature feature : insertFeatures) {
          //Add your logic here
        }
      }
    }
    //return accordingly
    return new SuccessResponse()
            .withStatus(String.format("Example success response for event with stream ID %s", event.getStreamId()));
  }
}
```

### 4. Add JUnits
To simulate and test the real-life example you have to follow few steps:
- Preparing an event: For testing purpose we are using json file from resources.
- Prepare context and Inject upstream-handler.
- Simulate connector config. 
- Simulate event-pipeline and call the handler.
```java
 @Test
    public void test() throws Exception {
        // Prepare event.
        final ModifyFeaturesEvent event = new ModifyFeaturesEvent();
        final ArrayList<com.here.naksha.lib.core.models.geojson.implementation.Feature> features = new ArrayList<>();
        final String topo1 = IoHelp.readResource("Topology1.json");
        //converting it from mom specific feature type to Naksha feature
        final com.here.naksha.lib.core.models.geojson.implementation.Feature topo1Feature = JsonSerializable.deserialize(topo1, com.here.naksha.lib.core.models.geojson.implementation.Feature.class);
        features.add(topo1Feature);
        event.setInsertFeatures(features);
        // Prepare context and inject our own upstream-handler.
        final TestEventContext context = new TestEventContext(event, (evt)->{
            assertNotNull(evt);
            return new ErrorResponse();
        });
        // Simulate connector config as it would come from Naksha-Hub (holding class name).
        final Connector connector = new Connector("test", ValidationHandler.class);
        // Create a new instance of the connector class.
        final IEventHandler handler = connector.newInstance();
        // Simulate the event-pipeline and call the handler.
        handler.processEvent(context);
    }

```

### 5. Compile and Build

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


### 6. Run on local

TODO :
* Steps to run XYZ Service on local
* Steps to run XYZ Extension on local
* Steps to configure Connector, Space in XYZ Service
* Steps to validate end-to-end REST API behaviour

