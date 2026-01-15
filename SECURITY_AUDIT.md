# Security Audit Report - ZeroTermux

**Audit Date**: 2026-01-15  
**Audit Version**: 1.0  
**Application Version**: 0.118.3.50 (versionCode 117)

## Executive Summary

This security audit identifies vulnerabilities and security concerns in the ZeroTermux application. Several **critical** and **high-severity** issues require immediate attention.

### Risk Level Summary
- 🔴 **Critical**: 4 issues
- 🟠 **High**: 6 issues  
- 🟡 **Medium**: 5 issues
- 🟢 **Low**: 3 issues

**Overall Risk Rating**: 🔴 **HIGH**

---

## 🔴 Critical Severity Issues

### 1. Apache Commons FileUpload - Remote Code Execution
**Severity**: Critical  
**Component**: `commons-fileupload:1.3.1`  
**CVE**: CVE-2016-1000031, CVE-2023-24998

**Description**:
The application uses an outdated version of Apache Commons FileUpload with known deserialization vulnerabilities that can lead to remote code execution.

**Attack Vector**:
- Malicious file upload with crafted headers
- Arbitrary code execution on the server/device
- Potential for privilege escalation

**Remediation**:
```gradle
// VULNERABLE
implementation group: 'commons-fileupload', name: 'commons-fileupload', version: '1.3.1'

// FIXED
implementation 'commons-fileupload:commons-fileupload:1.5'
```

**References**:
- https://nvd.nist.gov/vuln/detail/CVE-2016-1000031
- https://nvd.nist.gov/vuln/detail/CVE-2023-24998

---

### 2. Hardcoded Signing Credentials
**Severity**: Critical  
**Location**: `app/build.gradle` lines 28-38

**Description**:
The application contains hardcoded default values for signing credentials:
- Key Alias: `phone1`
- Key Password: `654321`
- Store Password: `123456`

**Security Impact**:
- Anyone with access to the repository can sign malicious APKs
- Compromises app integrity and user trust
- Enables supply chain attacks
- Violates security best practices

**Vulnerable Code**:
```gradle
signingConfigs {
    release {
        keyAlias System.getenv("KEY_ALIAS") ?: 'phone1'  // CRITICAL
        keyPassword System.getenv("KEY_PASSWORD") ?: '654321'  // CRITICAL
        storePassword System.getenv("STORE_PASSWORD") ?: '123456'  // CRITICAL
    }
}
```

**Remediation**:
```gradle
signingConfigs {
    release {
        // Fail build if credentials not provided
        keyAlias System.getenv("KEY_ALIAS")
        keyPassword System.getenv("KEY_PASSWORD")
        storeFile file(System.getenv("KEY_STORE_FILE") ?: "phone.jks")
        storePassword System.getenv("STORE_PASSWORD")
        
        // Validate credentials are set
        doFirst {
            if (!keyAlias || !keyPassword || !storePassword) {
                throw new GradleException("Signing credentials must be provided via environment variables")
            }
        }
    }
}
```

**Additional Steps**:
1. Rotate all signing keys immediately
2. Remove `phone.jks` from repository
3. Add `*.jks` to `.gitignore`
4. Use GitHub Secrets for CI/CD signing

---

### 3. Gson Deserialization Vulnerabilities
**Severity**: Critical  
**Component**: `com.google.code.gson:gson:2.8.6`  
**CVE**: Multiple CVEs related to unsafe deserialization

**Description**:
Outdated Gson version contains deserialization vulnerabilities that can lead to remote code execution when processing untrusted JSON data.

**Attack Vector**:
- Malicious JSON payloads
- Type confusion attacks
- Arbitrary code execution

**Remediation**:
```gradle
implementation 'com.google.code.gson:gson:2.10.1'
```

**Code Review Needed**:
- Review all Gson deserialization code
- Validate input before deserialization
- Use TypeToken for type safety
- Consider adding custom deserializers for sensitive data

---

### 4. JCenter Repository (Supply Chain Attack Risk)
**Severity**: Critical  
**Location**: `build.gradle` lines 7, 28

**Description**:
The project uses JCenter repository which was officially shutdown in February 2021. This creates several risks:
- Build failures when JCenter is completely unavailable
- Potential for dependency confusion attacks
- No security updates for JCenter-hosted libraries
- Stale dependencies

**Current Configuration**:
```gradle
repositories {
    jcenter()  // DEPRECATED AND SHUTDOWN
}
```

**Remediation**:
```gradle
repositories {
    google()
    mavenCentral()
    maven { url 'https://jitpack.io' }
    // Remove jcenter() entirely
}
```

**Verification Steps**:
1. Remove all jcenter() references
2. Run `./gradlew dependencies` to verify all dependencies resolve
3. Check for any JCenter-exclusive dependencies
4. Find alternatives on Maven Central or JitPack

---

## 🟠 High Severity Issues

### 5. Outdated Target SDK (Privacy & Security)
**Severity**: High  
**Current**: targetSdkVersion 28 (Android 9.0, 2018)  
**Required**: targetSdkVersion 34 (Android 14)

**Security Impact**:
- Missing runtime permission protections (Android 11+)
- No scoped storage enforcement (Android 10+)
- Missing notification permission (Android 13+)
- Background location restrictions not enforced
- Foreground service restrictions bypassed
- **Not eligible for Google Play Store** (requires SDK 31+)

**Privacy Concerns**:
- App has unrestricted access to storage
- Can access external storage without proper scoping
- Background location tracking without user awareness
- Notification spam without permission

**Remediation**:
Update `gradle.properties`:
```properties
targetSdkVersion=34
compileSdkVersion=34
```

**Required Code Changes**:
1. Implement scoped storage (Android 10+)
2. Request notification permission (Android 13+)
3. Add foreground service types (Android 14+)
4. Handle background restrictions
5. Update permission requests

---

### 6. Excessive Dangerous Permissions
**Severity**: High  
**Location**: `app/src/main/AndroidManifest.xml`

**Description**:
The application requests numerous high-risk permissions without clear justification:

```xml
<!-- Restricted Permissions -->
<uses-permission android:name="android.permission.MANAGE_EXTERNAL_STORAGE" />
<uses-permission android:name="android.permission.PACKAGE_USAGE_STATS" />

<!-- System-Level Permissions -->
<uses-permission android:name="android.permission.WRITE_SECURE_SETTINGS" />
<uses-permission android:name="android.permission.DUMP" />
<uses-permission android:name="android.permission.READ_LOGS" />

<!-- Sensitive Permissions -->
<uses-permission android:name="android.permission.READ_SMS" />
<uses-permission android:name="android.permission.READ_CONTACTS" />
<uses-permission android:name="android.permission.SYSTEM_ALERT_WINDOW" />
```

**Risk Assessment**:
- `MANAGE_EXTERNAL_STORAGE`: Unrestricted file access (play store restrictions)
- `WRITE_SECURE_SETTINGS`: Can modify system settings (requires system signature)
- `READ_SMS`: Privacy violation potential
- `READ_CONTACTS`: Privacy violation potential
- `PACKAGE_USAGE_STATS`: User behavior tracking
- `DUMP`: Debug permission (should not be in production)

**Remediation**:
1. Document necessity for each permission
2. Remove unnecessary permissions
3. Use scoped storage instead of MANAGE_EXTERNAL_STORAGE
4. Request permissions at runtime with clear explanations
5. Implement permission degradation (app works with subset)

---

### 7. Cleartext Traffic Enabled
**Severity**: High  
**Location**: `app/src/main/AndroidManifest.xml` line 65

**Description**:
```xml
android:usesCleartextTraffic="true"
```

This allows unencrypted HTTP traffic, exposing data to:
- Man-in-the-middle (MITM) attacks
- Packet sniffing
- Data interception
- Credential theft

**Remediation**:
Create `res/xml/network_security_config.xml`:
```xml
<?xml version="1.0" encoding="utf-8"?>
<network-security-config>
    <base-config cleartextTrafficPermitted="false">
        <trust-anchors>
            <certificates src="system" />
        </trust-anchors>
    </base-config>
    
    <!-- Only if absolutely necessary for specific domains -->
    <domain-config cleartextTrafficPermitted="true">
        <domain includeSubdomains="true">localhost</domain>
        <domain includeSubdomains="true">127.0.0.1</domain>
    </domain-config>
</network-security-config>
```

Update AndroidManifest.xml:
```xml
<application
    android:networkSecurityConfig="@xml/network_security_config"
    android:usesCleartextTraffic="false">
```

---

### 8. Disabled Stack Protection in Native Code
**Severity**: High  
**Location**: `app/build.gradle` line 166

**Description**:
Native code compilation disables stack protection:
```c
cFlags "-fno-stack-protector"
```

**Security Impact**:
- Buffer overflow exploits easier
- Stack-based attacks not detected
- Return-oriented programming (ROP) attacks viable
- No stack canaries for detection

**Remediation**:
```gradle
externalNativeBuild {
    ndkBuild {
        cFlags "-std=c11", "-Wall", "-Wextra", "-Werror", "-Os",
               "-fstack-protector-strong",  // Enable strong protection
               "-D_FORTIFY_SOURCE=2",        // Buffer overflow detection
               "-fPIE",                      // Position independent executable
               "-Wl,-z,relro,-z,now",       // Full RELRO
               "-Wl,--gc-sections"
    }
}
```

---

### 9. Guava Denial of Service Vulnerabilities
**Severity**: High  
**Component**: `com.google.guava:guava:31.0.1-jre`

**Description**:
Outdated Guava version contains DoS vulnerabilities in collection handling and string processing.

**Remediation**:
```gradle
implementation "com.google.guava:guava:33.0.0-jre"
```

---

### 10. Glide Memory Safety Issues
**Severity**: High  
**Component**: `com.github.bumptech.glide:glide:4.10.0`

**Description**:
Version 4.10.0 contains memory leak issues and improper image handling that can lead to crashes or OOM conditions.

**Remediation**:
```gradle
implementation 'com.github.bumptech.glide:glide:4.16.0'
annotationProcessor 'com.github.bumptech.glide:compiler:4.16.0'
```

---

## 🟡 Medium Severity Issues

### 11. Using Alpha/Unstable Dependencies
**Severity**: Medium  
**Component**: `androidx.core:core:1.9.0-alpha05`

**Description**:
Production app uses alpha-quality AndroidX libraries which may contain:
- Unpatched security vulnerabilities
- Unstable APIs
- Unexpected crashes
- Data loss bugs

**Remediation**:
```gradle
implementation "androidx.core:core:1.12.0"  // Use stable release
```

---

### 12. Outdated AndroidX Libraries
**Severity**: Medium  

Multiple outdated AndroidX dependencies with known security patches:
- `androidx.annotation:annotation:1.2.0` → 1.7.1
- `androidx.appcompat:appcompat:1.2.0` → 1.6.1  
- `androidx.constraintlayout:constraintlayout:2.0.1` → 2.1.4

**Impact**: Missing security patches and bug fixes

---

### 13. Legacy External Storage Access
**Severity**: Medium  
**Location**: `app/src/main/AndroidManifest.xml` line 61

**Description**:
```xml
android:requestLegacyExternalStorage="true"
```

This flag is deprecated and won't work on Android 11+ for new installs.

**Impact**:
- App will break on Android 11+ fresh installs
- Privacy concerns with unrestricted storage access

**Remediation**: Migrate to scoped storage

---

### 14. Lint Checks Disabled
**Severity**: Medium  
**Location**: `app/build.gradle` line 16

**Description**:
```gradle
lintOptions {
    checkReleaseBuilds false
}
```

**Impact**:
- Security issues not detected during build
- Potential bugs shipped to production
- No API compatibility checks

**Remediation**:
```gradle
lint {
    checkReleaseBuilds true
    abortOnError false  // Gradual migration
    warningsAsErrors false
    fatal 'StopShip'
    error 'HardcodedText', 'InvalidPackage'
}
```

---

### 15. Deprecated Kotlin Android Extensions
**Severity**: Medium  
**Location**: `build.gradle` line 19

**Description**:
Using deprecated `kotlin-android-extensions` plugin.

**Impact**:
- No security updates
- Won't work with newer Kotlin versions
- Can cause build failures

**Remediation**: Remove plugin (already using ViewBinding)

---

## 🟢 Low Severity Issues

### 16. Debuggable in Comments
**Severity**: Low  
**Location**: `app/src/main/AndroidManifest.xml` line 53

**Description**:
```xml
<!-- android:debuggable="true"-->
```

While commented out, this indicates the app may have been debuggable in the past.

**Best Practice**: Remove comment and ensure debuggable is never true in release builds.

---

### 17. Unsigned APK Distribution Risk
**Severity**: Low

**Description**:
If signing credentials are compromised (see Critical Issue #2), malicious APKs could be distributed.

**Mitigation**:
- Rotate signing keys
- Implement certificate pinning
- Use Google Play App Signing

---

### 18. No ProGuard/R8 Optimization
**Severity**: Low

**Description**:
While minification is enabled, could benefit from additional optimizations:
- Code shrinking optimization
- Resource shrinking optimization
- Obfuscation improvements

---

## Compliance Considerations

### Google Play Store Requirements
- ❌ **Target SDK**: Currently 28, requires 31+ (Critical blocker)
- ⚠️ **Permissions**: MANAGE_EXTERNAL_STORAGE may require justification
- ⚠️ **Privacy Policy**: Required for sensitive permissions
- ⚠️ **Data Safety**: Must declare data collection practices

### Privacy Regulations (GDPR, CCPA)
- ❌ **Data Collection**: Must document what data is collected
- ❌ **User Consent**: Need consent for SMS/Contacts access
- ❌ **Data Deletion**: Must provide data deletion mechanism
- ❌ **Privacy Policy**: Required and must be accessible

---

## Remediation Priority

### Immediate (This Week)
1. Remove hardcoded signing credentials
2. Rotate signing keys
3. Update Apache Commons FileUpload
4. Remove JCenter references
5. Update Gson

### Short Term (2-4 Weeks)
1. Update target SDK to 34
2. Remove excessive permissions
3. Disable cleartext traffic
4. Enable stack protection
5. Update critical dependencies

### Medium Term (1-2 Months)
1. Migrate to scoped storage
2. Implement runtime permissions properly
3. Add network security config
4. Update all AndroidX libraries
5. Enable lint checks

---

## Testing Recommendations

### Security Testing
- [ ] Penetration testing with MobSF or similar
- [ ] Static analysis with SonarQube
- [ ] Dependency scanning with OWASP Dependency-Check
- [ ] Dynamic analysis on actual devices
- [ ] Network traffic analysis with Burp Suite

### Compliance Testing  
- [ ] Permission usage audit
- [ ] Data collection audit
- [ ] Privacy policy review
- [ ] Store listing compliance check

---

## Security Best Practices Going Forward

1. **Dependency Management**
   - Enable Dependabot
   - Regular security audits
   - Subscribe to security mailing lists

2. **CI/CD Security**
   - Add CodeQL scanning
   - Implement secret scanning
   - Add SAST/DAST tools
   - Automated dependency updates

3. **Code Review**
   - Security-focused code review checklist
   - Mandatory review for permission changes
   - Review all network code
   - Review all file operations

4. **Runtime Protection**
   - Implement certificate pinning
   - Add runtime integrity checks
   - Implement anti-tampering measures
   - Add crash reporting with security filtering

---

## References

- OWASP Mobile Security Project: https://owasp.org/www-project-mobile-security/
- Android Security Best Practices: https://developer.android.com/topic/security/best-practices
- National Vulnerability Database: https://nvd.nist.gov/
- CWE/SANS Top 25: https://cwe.mitre.org/top25/

---

## Audit Contact

For questions about this security audit, please open an issue in the repository.

**Next Audit Recommended**: After completing critical and high-severity remediations

---

*Document Classification: Internal Use*  
*Distribution: Development Team, Security Team*  
*Last Updated: 2026-01-15*
