# Fork Maintenance Log

This file records the exact upstream and fork commit positions used to maintain the Codevkit Jilt fork.

The goal is to make every future upstream refresh reproducible: we should know which upstream commit the fork was based on, which fork commits make up the active fork-only feature stack, and which Maven version was published from that state.

## Rules

- Record one entry for every upstream sync or fork release.
- Record immutable commit SHAs, not only branch names.
- Record the complete active fork-only feature commit stack in every entry.
- Keep upstream sync commits separate from fork feature commits whenever possible.
- Update this file in the same commit that completes a fork refresh or release preparation.
- Do not use this file as a changelog for ordinary implementation details; it is a commit-position ledger.

## Entry Template

```text
## <fork-version-or-sync-name>

- Date:
- Upstream remote:
- Upstream base tag:
- Upstream base commit:
- Fork branch:
- Fork feature commits:
- Fork release commit:
- Published Maven version:
- Verification:
- Notes:
```

## Initial Context Builder Work

- Date: 2026-06-12
- Upstream remote: `git@github.com:skinny85/jilt.git`
- Upstream base tag: `1.9.1`
- Upstream base commit: `b7f356da8cb61250bfa8657d82cb8c7ed3e7da45`
- Fork branch: `master`
- Fork feature commits:
  - `fc4bc4afcbb98b1fb1725a8496468806d3c8c37e` - Add context builder support
- Fork release commit: `f10cd4d5980f409b587d5e71d4aad77bde8e6762`
- Published Maven version: `1.9.1-fork.1`
- Verification:
  - `./gradlew test --rerun-tasks`
  - `./gradlew generatePomFileForShadowPublication`
  - `./gradlew publishToMavenLocal`
  - `./gradlew clean build check`
  - `./gradlew publishShadowPublicationToMavenCentralRepository -PpublishToMavenCentral=true`
  - Central Portal deployment `e8e0b4b2-cc3c-4575-98b8-9f635d3ffdd1` published successfully.
- Notes: Initial fork-only implementation adds context builder support through `@Builder` `contextType` and `contextMethod`.

## 1.9.1-fork.2

- Date: 2026-06-12
- Upstream remote: `git@github.com:skinny85/jilt.git`
- Upstream base tag: `1.9.1`
- Upstream base commit: `b7f356da8cb61250bfa8657d82cb8c7ed3e7da45`
- Fork branch: `master`
- Fork feature commits:
  - `fc4bc4afcbb98b1fb1725a8496468806d3c8c37e` - Add context builder support
  - `56e75dc3178f79519467cb2ced912a78e62bcdba` - Support placeholders in builder interface names
- Fork release commit: `b5b65ac79c3fa5b7415a020df3295ba8726a300c`
- Published Maven version: `1.9.1-fork.2`
- Verification:
  - `JAVA_HOME=~/.sdkman/candidates/java/17.0.14-jbr ./gradlew clean build check`
  - `JAVA_HOME=~/.sdkman/candidates/java/17.0.14-jbr ./gradlew publishToMavenLocal`
  - `javap -verbose -classpath build/libs/jilt-1.9.1-fork.2.jar org.jilt.Builder | rg 'major version'` reported `major version: 61`.
  - `JAVA_HOME=~/.sdkman/candidates/java/17.0.14-jbr ./gradlew publishShadowPublicationToMavenCentralRepository -PpublishToMavenCentral=true`
  - Central Portal deployment `eb8d19d0-6c7c-4305-ba8b-15f7aa63eb1a` published successfully.
- Notes: Releases the builder interface placeholder fix and records the Codevkit fork Java 17 baseline.

## Upstream 1.9.2 Sync

- Date: 2026-09-29
- Upstream remote: `git@github.com:skinny85/jilt.git`
- Upstream base tag: `1.9.2`
- Upstream base commit: `53085b0f7cba2c4c089d03bdf23dab033b11aaf7`
- Fork branch: `master`
- Fork feature commits:
  - `fc4bc4afcbb98b1fb1725a8496468806d3c8c37e` - Add context builder support
  - `56e75dc3178f79519467cb2ced912a78e62bcdba` - Support placeholders in builder interface names
- Fork release commit: not released
- Published Maven version: not published
- Verification:
  - `JAVA_HOME=~/.sdkman/candidates/java/17.0.14-jbr ./gradlew clean build check`
  - `JAVA_HOME=~/.sdkman/candidates/java/17.0.14-jbr ./gradlew publishToMavenLocal`
- Notes: Merges upstream `1.9.2`, including required nullable properties through `@Req`, while retaining the Codevkit context builder extensions and independent Maven coordinates. The next fork release is staged as `1.9.2-fork.1-SNAPSHOT`.

## 1.9.2-fork.1

- Date: 2026-09-29
- Upstream remote: `git@github.com:skinny85/jilt.git`
- Upstream base tag: `1.9.2`
- Upstream base commit: `53085b0f7cba2c4c089d03bdf23dab033b11aaf7`
- Fork branch: `master`
- Fork feature commits:
  - `fc4bc4afcbb98b1fb1725a8496468806d3c8c37e` - Add context builder support
  - `56e75dc3178f79519467cb2ced912a78e62bcdba` - Support placeholders in builder interface names
- Fork release commit: `1634eb323a0597b98e838856094858b7f376f8e8`
- Published Maven version: `1.9.2-fork.1`
- Verification:
  - `JAVA_HOME=~/.sdkman/candidates/java/17.0.14-jbr ./gradlew clean build check`
  - `JAVA_HOME=~/.sdkman/candidates/java/17.0.14-jbr ./gradlew publishToMavenLocal`
  - `javap -verbose -classpath build/libs/jilt-1.9.2-fork.1.jar org.jilt.Builder | rg 'major version'` reported `major version: 61`.
  - GitHub Actions build `36532149972` passed on Java 8, 11, 17, and 21.
  - `JAVA_HOME=~/.sdkman/candidates/java/17.0.14-jbr ./gradlew publishShadowPublicationToMavenCentralRepository -PpublishToMavenCentral=true`
  - Central Portal deployment `7b8da6ca-789e-4ea0-a9d0-d7bf20c10b97` reached `PUBLISHED`.
  - The public Maven Central POM returned HTTP 200.
- Notes: First Codevkit fork release based on upstream `1.9.2`. It retains context builder support and builder interface placeholders while adding upstream `@Req` support.
