# Codevkit Fork Release

This fork publishes independently from upstream Jilt.

## Coordinates

```text
io.github.codevkit.jilt:jilt:1.9.1-fork.2
```

Keep fork releases on the upstream-version-plus-fork-counter line:

```text
<upstream-version>-fork.<n>
```

For example, the first Codevkit release based on upstream `1.9.1` is `1.9.1-fork.1`.

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

Do not publish this fork with JDK 25 while it uses Gradle 8.5; Gradle 8.5 does not reliably support that runtime. Do not use JDK 8 for new fork releases unless the fork policy changes back to upstream Java 8 bytecode compatibility.

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

Publish the fork artifact to Maven Central:

```bash
JAVA_HOME=~/.sdkman/candidates/java/17.0.14-jbr ./gradlew publishShadowPublicationToMavenCentralRepository -PpublishToMavenCentral=true
```

After the Central deployment is published, update `docs/fork-maintenance-log.md` with:

- the exact fork release commit,
- the published Maven version,
- the verification commands that were run,
- any Maven Central deployment identifier or release notes that matter later.
