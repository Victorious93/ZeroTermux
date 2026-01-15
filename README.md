# ZeroTermux

ZeroTermux is a non-profit software developed based on [Termux](https://termux.dev/), featuring internal channel implementation of X11 AAR.

**X11 AAR Implementation:** https://github.com/hanxinhao000/ZeroTermux-X11-aar

## Table of Contents

- [Download](#download)
- [Features](#features)
- [Build Instructions](#build-instructions)
  - [Prerequisites](#prerequisites)
  - [Building from Source](#building-from-source)
- [Default Sources](#default-sources)
- [Language Support](#language-support)
- [Recommendation](#recommendation)
- [Statement and License](#statement-and-license)
- [Projects Referenced](#projects-referenced)
- [Acknowledgments](#acknowledgments)

## Download

### Pre-compiled APK

If you don't want to compile it yourself, you can download a pre-compiled version.

**Note:** Don't click directly—copy the address and paste it into your browser's address bar:
```
https://od.ixcmstudio.cn/repository/main/ZeroTermux/
```

**Signing File:** ZeroTermux signing file is shared with Utermux.

## Features

ZeroTermux extends the standard Termux functionality with additional features:

- **Backup and Recovery:** Complete system backup and restore capabilities
- **Container Switching:** Easy switching between different environments
- **Source Management:** Built-in source switching functionality
- **Linux Distributions:** Support for Ubuntu, Kali, and other distributions
- **And More:** Various enhancements and improvements

## Build Instructions

### Prerequisites

Before building ZeroTermux, ensure you have the following installed:

- **Java Development Kit (JDK):**
  - JDK 17 for Android SDK tools
  - JDK 11 for compilation
- **Android SDK:**
  - Platform: Android 13 (API level 33)
  - Build Tools: 30.0.2
  - NDK: 27.0.12077973 and 22.1.7171670
- **Git:** For cloning the repository with submodules

### Building from Source

1. **Clone the repository:**
   ```bash
   git clone https://github.com/Victorious93/ZeroTermux.git
   cd ZeroTermux
   git submodule update --init --recursive
   ```

2. **Set execute permissions for Gradle wrapper:**
   ```bash
   chmod +x gradlew
   ```

3. **Build the APK:**
   
   For debug build:
   ```bash
   ./gradlew assembleDebug
   ```
   
   For release build:
   ```bash
   ./gradlew assembleRelease
   ```
   
   For both debug and release:
   ```bash
   ./gradlew assembleDebug assembleRelease --stacktrace
   ```

4. **Find the built APK:**
   - Debug APK: `app/build/outputs/apk/debug/`
   - Release APK: `app/build/outputs/apk/release/`

## Default Sources

The default package sources are configured as follows:

- **Tsinghua Mirror**
- **Beijing Mirror**

**Important:** These sources must be switched manually and are optimized for domestic (China) users. Foreign users should use the official Termux version instead.

## Language Support

- Chinese (中文)
- English

## Recommendation

After using ZeroTermux for a while, it is strongly recommended that you transition to the official Termux once you're comfortable with the basics.

## Links

- **Original Termux:** [Visit official Termux](https://termux.dev/)
- **Alibaba ICO Library:** [Visit Alibaba ICO](https://www.iconfont.cn/)

## Statement and License

**All functions of ZeroTermux may only be used for personal learning and communication; commercial or illegal use is prohibited.**

1. This software (ZeroTermux) is open-source; you may freely distribute, copy, and modify it, but you must comply with the GPL license.

2. The software follows the GPL license: http://www.opensource.org/licenses/gpl-2.0.php

3. All functions are intended solely for learning and communication. Any accidental, intentional, or inadvertent actions taken by users of ZeroTermux are the users' responsibility.

4. ZeroTermux is free for learning and communication as stated in clause 1. Icons and fonts used in the app come from the internet.
   - **Fonts:** Default Android fonts; if you use other fonts, the user assumes all risks and legal liabilities.
   - **Icons:** From Alibaba's public ICO library, used only for learning and communication, not commercial purposes. Any infringement will be removed immediately.

5. Recovery packs, data packs, zip files, etc., used within this software carry inherent risks and are sourced from the internet. Users should use them at their own discretion; any irreversible loss is the user's responsibility.

6. ZeroTermux cannot control the content of recovery packs, data packs, zip files, or module packages. If any pack contains infringing material, the author of ZeroTermux bears no liability; responsibility lies with the pack's creator (legal statement for tar.gz backup function).

7. Thanks to all authors whose functionalities are incorporated into this project.

8. Using ZeroTermux may cause direct or indirect damage to your device. In case of irreparable loss, the user assumes responsibility; the software and its author are not liable for any legal consequences.

## Projects Referenced

ZeroTermux incorporates functionality from the following open-source projects:

- [termux/termux-app](https://github.com/termux/termux-app)
- [termux/termux-tasker](https://github.com/termux/termux-tasker)
- [termux/termux-api](https://github.com/termux/termux-api)
- [termux/termux-styling](https://github.com/termux/termux-styling)
- [termux/termux-packages](https://github.com/termux/termux-packages)
- [Lichenwei-Dev/ImagePicker](https://github.com/Lichenwei-Dev/ImagePicker)
- [BryleHelll/android-vshell](https://github.com/BryleHelll/android-vshell)
- [Justson/AgentWeb](https://github.com/Justson/AgentWeb)
- [getActivity/XXPermissions](https://github.com/getActivity/XXPermissions)
- [magnusja/libaums](https://github.com/magnusja/libaums)
- [rtugeek/ColorSeekBar](https://github.com/rtugeek/ColorSeekBar)
- [jeasonlzy/ImagePicker](https://github.com/jeasonlzy/ImagePicker)
- [bumptech/glide](https://github.com/bumptech/glide)
- [tsl0922/ttyd](https://github.com/tsl0922/ttyd)
- [filebrowser/filebrowser](https://github.com/filebrowser/filebrowser)
- [Alibaba Iconfont](https://www.iconfont.cn/)
- [ppareit/swiftp](https://github.com/pparert/swiftp)
- [gyf-dev/ImmersionBar](https://github.com/gyf-dev/ImmersionBar)
- [570622566/FNetServer](https://github.com/570622566/FNetServer)
- [testica/codeeditor](https://github.com/testica/codeeditor)

## Acknowledgments

ZeroTermux thanks all the authors whose work is referenced in this project. The project presentation may not be exhaustive; thank you for your understanding.

**If you find the project useful, please consider supporting it!**
