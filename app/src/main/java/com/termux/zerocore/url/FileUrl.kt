package com.termux.zerocore.url

import android.os.Environment
import com.termux.shared.termux.TermuxConstants
import java.io.File


/**
 *
 * Path management
 *
 *
 */
object FileUrl {

    // Main directory
    public val mainFilesUrl = TermuxConstants.TERMUX_FILES_DIR_PATH
    public val mainAppUrl = TermuxConstants.TERMUX_INTERNAL_PRIVATE_APP_DATA_DIR_PATH
    public val mainHomeUrl = TermuxConstants.TERMUX_FILES_DIR_PATH + "/home"
    public val mainBinUrl = TermuxConstants.TERMUX_FILES_DIR_PATH + "/usr/bin"
    public val mainConfigUrl = TermuxConstants.TERMUX_FILES_DIR_PATH + "/home/.termux/"
    public val mainConfigImg = TermuxConstants.TERMUX_FILES_DIR_PATH + "/home/.img/"


    // Main directory
    public val zeroTermuxHome = File(Environment.getExternalStorageDirectory(), "/xinhao/")
    // Restore directory
    public val zeroTermuxData = File(Environment.getExternalStorageDirectory(), "/xinhao/data/")
    // APK directory
    public val zeroTermuxApk = File(Environment.getExternalStorageDirectory(), "/xinhao/apk/")
    // Windows directory
    public val zeroTermuxWindows = File(Environment.getExternalStorageDirectory(), "/xinhao/windows/")
   // Command directory
    public val zeroTermuxCommand = File(Environment.getExternalStorageDirectory(), "/xinhao/command/")
   // Font directory
    public val zeroTermuxFont = File(Environment.getExternalStorageDirectory(), "/xinhao/font")
   // ISO directory
    public val zeroTermuxIso = File(Environment.getExternalStorageDirectory(), "/xinhao/iso")
   // MySQL directory
    public val zeroTermuxMysql = File(Environment.getExternalStorageDirectory(), "/xinhao/mysql")
    // online_system directory
    public val zeroTermuxOnlineSystem = File(Environment.getExternalStorageDirectory(), "/xinhao/online_system")
   // QEMU directory
    public val zeroTermuxQemu = File(Environment.getExternalStorageDirectory(), "/xinhao/qemu")
   // Server directory
    public val zeroTermuxServer = File(Environment.getExternalStorageDirectory(), "/xinhao/server")
   // Share directory
    public val zeroTermuxShare = File(Environment.getExternalStorageDirectory(), "/xinhao/share")
    // System directory
    public val zeroTermuxSystem = File(Environment.getExternalStorageDirectory(), "/xinhao/system")
    // web_config
    public val zeroTermuxWebConfig = File(Environment.getExternalStorageDirectory(), "/xinhao/web_config")
    // Module directory
    public val zeroTermuxModule = File(Environment.getExternalStorageDirectory(), "/xinhao/module")
     val zeroTermuxWindowsConfig = File(Environment.getExternalStorageDirectory(), "/xinhao/windows_config/")

    // Official sources path [source path]
    public val sourcesUrl = "${TermuxConstants.TERMUX_FILES_DIR_PATH}/usr/etc/apt/sources.list"
    // Official science path [source path]
    public val scienceUrl = "${TermuxConstants.TERMUX_FILES_DIR_PATH}/usr/etc/apt/sources.list.d/science.list"
    // Official game path [source path]
    public val gameUrl = "${TermuxConstants.TERMUX_FILES_DIR_PATH}/usr/etc/apt/sources.list.d/game.list"
    // SMS tool directory
    public val smsUrl = "${TermuxConstants.TERMUX_FILES_DIR_PATH}/usr/bin/smsread"
    // Contacts tool directory
    public val phoneUrl = "${TermuxConstants.TERMUX_FILES_DIR_PATH}/usr/bin/readcontacts"

    // Open left tool
    public val openLeft = "${TermuxConstants.TERMUX_FILES_DIR_PATH}/usr/bin/openleftwindow"
    // Open right tool
    public val openRight = "${TermuxConstants.TERMUX_FILES_DIR_PATH}/usr/bin/openrightwindow"
    // ZT general tool
    public val zt = "${TermuxConstants.TERMUX_FILES_DIR_PATH}/usr/bin/zt"
    // Channel file APK
    public val aislePathAPK = "${TermuxConstants.TERMUX_FILES_DIR_PATH}/usr/libexec/termux-x11/loader.apk"
    public val aislePathAPKPath = "${TermuxConstants.TERMUX_FILES_DIR_PATH}/usr/libexec/termux-x11"
    // Channel file execution script
    public val aislePathSh = "${TermuxConstants.TERMUX_FILES_DIR_PATH}/usr/bin/termux-x11"
    // Channel file execution script
    public val aislePreferencePathSh = "${TermuxConstants.TERMUX_FILES_DIR_PATH}/usr/bin/termux-x11-preference"
    // Channel binary file
    public val aislePathSo = "${TermuxConstants.TERMUX_FILES_DIR_PATH}/usr/lib/libXlorie.so"
    // Timer directory
    public val timerTermuxDir = "${mainHomeUrl}/.timerdir"
    public val timerTermuxFile = "${mainHomeUrl}/.timerdir/termux_timer.sh"

    public val timerShellDir = "${mainHomeUrl}/.timerdir"
    public val timerShellFile = "${mainHomeUrl}/.timerdir/shell_timer.sh"
    public val timerShellLogDir = "${mainHomeUrl}/.timerdir/log"
    public val timerShellExecDir = "${mainFilesUrl}"
    public val timerShellExecFile = "${mainFilesUrl}/execTermuxEnv.sh"


    // Get SMS directory
    public val smsUrlFile = "${TermuxConstants.TERMUX_FILES_DIR_PATH}/home/sms.txt"
    public val phoneUrlFile = "${TermuxConstants.TERMUX_FILES_DIR_PATH}/home/phone.txt"
    // System boot script directory
    ///data/data/com.termux/files/usr/etc/bash.bashrc  .xinhao_history
    public val smsBashrcFile = "${TermuxConstants.TERMUX_FILES_DIR_PATH}/usr/etc/bash.bashrc"
    public val smsMotdFile = "${TermuxConstants.TERMUX_FILES_DIR_PATH}/usr/etc/motd"
    // Zero system script directory
    public val smsZeroBashrcFileD = "${TermuxConstants.TERMUX_FILES_DIR_PATH}/home/.xinhao_history"
    // Zero system script
    public val smsZeroBashrcFile = "${TermuxConstants.TERMUX_FILES_DIR_PATH}/home/.xinhao_history/start_command.sh"



}
