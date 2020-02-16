# webrtc android client

WebRTC android client.

For backend use [webrtc.server](https://github.com/ranapat/webrtc.server)

## Requirements
* Java 8
* Android SDK
* Gradle

## Building

Build tool is gradle

### Assemble
Run `./gradlew assemble`

### Run unit tests
Run `./gradlew test`

### Run ui tests
Run `./gradlew connectedCheck`

### Run lint
Run `./gradlew lint`

### Run all development tests
This will run lintDevelopmentDebug and testDevelopmentDebugUnitTestCoverage

You can find the outputs here:
- for the lint `./app/build/reports/lint-results-developmentDebug.html`
- for the unit test coverage `./app/build/reports/jacoco/testDevelopmentDebugUnitTestCoverage/html/index.html`
- for the unit test summary `./app/build/reports/tests/testDevelopmentDebugUnitTest/index.html`

Run `./gradlew devTests`

### Before running adjust constants in `./build.gradle`

```
  gateway_api_development = "http://<server.ip>:<server.port>"
  gateway_api_production = "http://<server.ip>:<server.port>"
 
  streams_api_development = "http://<server.ip>:<server.port>/streams.json"
  streams_api_production = "http://<server.ip>:<server.port>/streams.json"
```
