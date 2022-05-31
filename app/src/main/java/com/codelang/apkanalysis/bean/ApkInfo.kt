package com.codelang.apkanalysis.bean

import android.content.pm.PackageInfo
import android.graphics.drawable.Drawable
import android.os.Build
import com.codelang.apkanalysis.utils.PackageUtils
import com.codelang.apkanalysis.utils.SignType
import com.codelang.apkanalysis.utils.SigningUtils
import java.io.Serializable
import java.text.SimpleDateFormat

data class ApkInfo(
    val appName: String? = "",
    @Transient
    val icon: Drawable? = null,
    var targetSdk: Int = 0,
    var minSdk: Int = -1,
    var packageName: String? = "",
    var versionName: String? = "",
    var versionCode: Long = 0L,
    var SHA1: String? = "",
    var MD5: String? = "",
    var SHA256: String? = "",
    var apkPath: String? = "",
    var nativeLibraryDir: String = "",
    var firstInstallTime: String = "",
    var lastUpdateTime: String = "",
) : Serializable


fun PackageInfo.toApkInfo(): ApkInfo {
    val icon = applicationInfo.loadIcon(PackageUtils.packageManager)
    val name =
        applicationInfo.loadLabel(PackageUtils.packageManager).toString()
    val targetSdk = applicationInfo.targetSdkVersion
    val minSdk = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        applicationInfo.minSdkVersion
    } else {
        -1
    }
    val packageName = packageName
    val versionName = versionName
    val versionCode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        longVersionCode
    } else {
        versionCode.toLong()
    }
    val SHA1 = SigningUtils.getSign(SignType.SHA1, this) ?: ""
    val SHA256 = SigningUtils.getSign(SignType.SHA256, this) ?: ""
    val MD5 = SigningUtils.getSign(SignType.MD5, this) ?: ""
    val apkPath = this.applicationInfo.publicSourceDir
    val nativeLibraryDir = this.applicationInfo.nativeLibraryDir

    val firstInstallTime = firstInstallTime.toTime()
    val lastUpdateTime = lastUpdateTime.toTime()

    return ApkInfo(
        appName = name,
        icon = icon,
        targetSdk = targetSdk,
        minSdk = minSdk,
        packageName = packageName,
        versionName = versionName,
        versionCode = versionCode,
        SHA1 = SHA1,
        MD5 = MD5,
        SHA256 = SHA256,
        apkPath = apkPath,
        nativeLibraryDir = nativeLibraryDir,
        firstInstallTime = firstInstallTime,
        lastUpdateTime = lastUpdateTime
    )
}

fun Long.toTime(): String {
    return SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(this)
}