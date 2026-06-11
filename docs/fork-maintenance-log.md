# Fork Maintenance Log

This file records the exact upstream and fork commit positions used to maintain the Codevkit Jilt fork.

The goal is to make every future upstream refresh reproducible: we should know which upstream commit the fork was based on, which fork commit introduced or refreshed fork-only changes, and which Maven version was published from that state.

## Rules

- Record one entry for every upstream sync or fork release.
- Record immutable commit SHAs, not only branch names.
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
- Fork feature commit:
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
- Fork feature commit: `fc4bc4afcbb98b1fb1725a8496468806d3c8c37e`
- Fork release commit: pending.
- Published Maven version: pending. Planned first fork version: `1.9.1-fork.1`.
- Verification: `/Users/ylw/Zero/jilt/gradlew -p /Users/ylw/Zero/jilt test --rerun-tasks`
- Notes: Initial fork-only implementation adds context builder support through `@Builder` `contextType` and `contextMethod`.
