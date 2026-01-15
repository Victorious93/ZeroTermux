# ZeroTermux Upgrade Checklist

Quick reference checklist for upgrading the ZeroTermux application. See [UPGRADE_SUGGESTIONS.md](./UPGRADE_SUGGESTIONS.md) for detailed information.

## 🔴 Critical Priority (Do First)

### Security Vulnerabilities
- [ ] Update Gson from 2.8.6 to 2.10.1 (CVE fixes)
- [ ] Update Apache Commons FileUpload from 1.3.1 to 1.5 (CVE-2016-1000031, CVE-2023-24998)
- [ ] Update Guava from 31.0.1-jre to 33.0.0-jre (DoS vulnerabilities)
- [ ] Remove hardcoded signing credentials from build.gradle
- [ ] Review and minimize dangerous permissions in AndroidManifest.xml

### Deprecated/Broken Dependencies
- [ ] Remove all `jcenter()` references (JCenter is shutdown)
- [ ] Replace deprecated `compile()` with `implementation()` (line 138 in app/build.gradle)
- [ ] Replace androidx.core:core alpha version with stable release
- [ ] Remove deprecated kotlin-android-extensions plugin

## 🟠 High Priority (Do Soon)

### Build System
- [ ] Update Android Gradle Plugin from 4.2.2 to 8.2.0+
- [ ] Update Gradle wrapper to 8.2+
- [ ] Update Kotlin from 1.7.20 to 1.9.22+
- [ ] Align NDK versions (currently inconsistent: 27.0.12077973 vs 22.1.7171670)
- [ ] Update Java compatibility from 8 to 17

### Android API Levels
- [ ] Update targetSdkVersion from 28 to 34 (required for Google Play)
- [ ] Update compileSdkVersion from 33 to 34
- [ ] Consider updating minSdkVersion from 23 to 24

### Android 13+ Compatibility
- [ ] Add POST_NOTIFICATIONS permission handling
- [ ] Implement runtime notification permission request
- [ ] Migrate to scoped storage (from MANAGE_EXTERNAL_STORAGE)
- [ ] Add foreground service types for Android 14

### AndroidX Libraries
- [ ] Update androidx.core:core from 1.9.0-alpha05 to 1.12.0
- [ ] Update androidx.appcompat from 1.2.0 to 1.6.1
- [ ] Update androidx.annotation from 1.2.0 to 1.7.1
- [ ] Update androidx.constraintlayout from 2.0.1 to 2.1.4
- [ ] Update androidx.lifecycle from 2.5.1 to 2.7.0
- [ ] Update Material Design from 1.2.1 to 1.11.0

## 🟡 Medium Priority (Plan For)

### Other Dependencies
- [ ] Update Glide from 4.10.0 to 4.16.0
- [ ] Update Kotlin Coroutines from 1.4.2 to 1.8.0
- [ ] Update Robolectric from 4.4 to 4.11.1
- [ ] Review and update all other third-party dependencies

### Code Quality
- [ ] Enable lint checks (currently disabled with `checkReleaseBuilds false`)
- [ ] Remove `includeCompileClasspath true` from annotation processor options
- [ ] Update ProGuard/R8 rules for full mode
- [ ] Review and fix lint warnings

### Security Hardening
- [ ] Add native security flags: `-fstack-protector-strong`, `-D_FORTIFY_SOURCE=2`
- [ ] Create network security configuration (disable cleartext traffic)
- [ ] Review all requested permissions and remove unnecessary ones
- [ ] Update signing configuration to require environment variables

## 🟢 Low Priority (Nice to Have)

### CI/CD
- [ ] Uncomment and update GitHub Actions workflow
- [ ] Add Dependabot configuration
- [ ] Add CodeQL security scanning
- [ ] Add automated dependency updates

### Documentation
- [ ] Add CHANGELOG.md
- [ ] Add SECURITY.md policy
- [ ] Update README with badges and better instructions
- [ ] Add CONTRIBUTING.md
- [ ] Document architecture decisions (ADRs)

### Architecture
- [ ] Consider Jetpack Compose for new UI features
- [ ] Implement modern architecture (MVVM/MVI)
- [ ] Add dependency injection (Hilt/Koin)
- [ ] Improve test coverage
- [ ] Consider app modularization

## Progress Tracking

Use this template to track your progress:

```markdown
## Phase 1: Critical Issues (Week 1)
Started: YYYY-MM-DD
Completed: YYYY-MM-DD
Status: ⏳ In Progress / ✅ Complete / ❌ Blocked

## Phase 2: Build System (Week 2)
Started: YYYY-MM-DD
Completed: YYYY-MM-DD
Status: 📋 Not Started

## Phase 3: Android API Updates (Week 3-4)
Started: YYYY-MM-DD
Completed: YYYY-MM-DD
Status: 📋 Not Started
```

## Testing Checklist

After each major change, verify:

- [ ] App builds successfully (debug and release)
- [ ] App installs and launches on test devices
- [ ] All main features work correctly:
  - [ ] Terminal emulation
  - [ ] File browser
  - [ ] Package management
  - [ ] Container switching
  - [ ] Backup/restore
  - [ ] X11 integration
- [ ] Test on multiple Android versions:
  - [ ] Android 6.0 (API 23) - Minimum
  - [ ] Android 9.0 (API 28) - Previous target
  - [ ] Android 12 (API 31) - Play Store minimum
  - [ ] Android 13 (API 33)
  - [ ] Android 14 (API 34) - New target
- [ ] No new crashes or ANRs
- [ ] Performance is acceptable
- [ ] Security scan passes (if available)

## Quick Command Reference

```bash
# Update Gradle wrapper
./gradlew wrapper --gradle-version=8.2

# Check for outdated dependencies
./gradlew dependencyUpdates

# Run lint checks
./gradlew lint

# Build debug APK
./gradlew assembleDebug

# Build release APK
./gradlew assembleRelease

# Run tests
./gradlew test

# Clean build
./gradlew clean build
```

## Rollback Checklist

If something goes wrong:

- [ ] Check Git status and recent commits
- [ ] Identify the problematic change
- [ ] Revert specific commits: `git revert <commit-hash>`
- [ ] Test that rollback works
- [ ] Document what went wrong
- [ ] Plan a better approach

## Notes

- Keep this checklist updated as you progress
- Mark items as complete with `[x]`
- Add notes about any issues encountered
- Link to related issues/PRs for tracking

---

*Last Updated: 2026-01-15*
