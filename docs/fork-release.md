# Codevkit Fork Release

This fork publishes independently from upstream Jilt.

## Coordinates

```text
io.github.codevkit.jilt:jilt:1.9.2-fork.1
```

Keep fork releases on the upstream-version-plus-fork-counter line:

```text
<upstream-version>-fork.<n>
```

For example, the first Codevkit release based on upstream `1.9.2` is `1.9.2-fork.1`.

## Java Baseline

Codevkit fork releases follow the mainstream framework baseline used by current Quarkus and Spring generations:

```text
Java 17
```

Build and publish releases with JDK 17 explicitly configured:

```bash
JAVA_HOME=~/.sdkman/candidates/java/17.0.14-jbr ./gradlew clean build check
JAVA_HOME=~/.sdkman/candidates/java/17.0.14-jbr ./gradlew publishToMavenLocal
JAVA_HOME=~/.sdkman/candidates/java/17.0.14-jbr ./gradlew publishShadowPublicationToMavenCentralRepository -PpublishToMavenCentral=true
```

The expected class file version for fork releases is:

```text
major version: 61
```

Verify it before publishing:

```bash
javap -verbose -classpath build/libs/jilt-<version>.jar org.jilt.Builder | rg 'major version'
```

The wrapper currently uses Gradle 8.14.5, but wrapper runtime compatibility does not change the fork's release baseline. Publish with JDK 17 so release artifacts consistently target Java 17 bytecode. Do not use JDK 8 or a newer JDK for a fork release unless this policy and the expected class file version are deliberately updated first.

## Verification

Run the full project verification before publishing:

```bash
JAVA_HOME=~/.sdkman/candidates/java/17.0.14-jbr ./gradlew clean build check
```

Generate and inspect the Maven publication locally:

```bash
JAVA_HOME=~/.sdkman/candidates/java/17.0.14-jbr ./gradlew publishToMavenLocal
```

The generated POM should use the Codevkit fork coordinates and SCM:

```text
io.github.codevkit.jilt:jilt
https://github.com/codevkit/jilt
```

## Maven Central Credentials

Publishing reads credentials from Gradle properties or environment variables:

```text
mavenCentralUsername / MAVEN_CENTRAL_USERNAME / SONATYPE_USERNAME
mavenCentralPassword / MAVEN_CENTRAL_PASSWORD / SONATYPE_PASSWORD
signingKey / SIGNING_KEY
signingPassword / SIGNING_PASSWORD
```

Legacy `ossrhUsername` and `ossrhPassword` properties are still accepted.

## Publish

Upload the signed fork artifacts to the OSSRH Staging API compatibility service:

```bash
JAVA_HOME=~/.sdkman/candidates/java/17.0.14-jbr \
  ./gradlew publishShadowPublicationToMavenCentralRepository -PpublishToMavenCentral=true
```

Gradle's built-in `maven-publish` plugin only uploads files. Transfer the completed staging repository into the Central Publisher Portal with the Portal token credentials used by Gradle. For this command, expose them as `MAVEN_CENTRAL_USERNAME` and `MAVEN_CENTRAL_PASSWORD` environment variables even if Gradle read them from properties:

```bash
CENTRAL_AUTH="$(printf '%s' \
  "${MAVEN_CENTRAL_USERNAME}:${MAVEN_CENTRAL_PASSWORD}" \
  | base64 | tr -d '\n')"
printf 'header = "Authorization: Bearer %s"\n' "${CENTRAL_AUTH}" \
  | curl --config - --fail-with-body --request POST \
      'https://ossrh-staging-api.central.sonatype.com/manual/upload/defaultRepository/io.github.codevkit?publishing_type=user_managed'
unset CENTRAL_AUTH
```

Wait for the deployment to reach `VALIDATED`, review it in the Portal, and publish it. The deployment will not appear in the Portal until the manual upload request succeeds.

After the Central deployment is published, update `docs/fork-maintenance-log.md` with:

- the exact fork release commit,
- the published Maven version,
- the verification commands that were run,
- any Maven Central deployment identifier or release notes that matter later.
