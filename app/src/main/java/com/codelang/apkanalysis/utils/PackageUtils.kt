package com.codelang.apkanalysis.utils

import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Build
import androidx.annotation.ChecksSdkIntAtLeast
import com.codelang.apkanalysis.App

object PackageUtils {

    val packageManager: PackageManager by lazy { App.app.packageManager }

    /**
     * Get all installed apps in device
     * @return list of apps
     * @throws Exception
     */
    @Throws(Exception::class)
    fun getInstallApplications(): List<PackageInfo> {
        return packageManager.getInstalledPackages(
            VersionCompat.MATCH_UNINSTALLED_PACKAGES
                    or PackageManager.GET_META_DATA
                    or PackageManager.GET_PERMISSIONS
                    or PackageManager.GET_SIGNATURES
                    or PackageManager.GET_ACTIVITIES
                    or PackageManager.GET_META_DATA
                    or PackageManager.GET_RECEIVERS
                    or PackageManager.GET_SERVICES
                    or PackageManager.GET_PROVIDERS
        )

    }


    fun getPackageInfo(packageName: String): PackageInfo {
        return packageManager.getPackageInfo(
            packageName,
            VersionCompat.MATCH_UNINSTALLED_PACKAGES
                    or PackageManager.GET_META_DATA
                    or PackageManager.GET_PERMISSIONS
                    or PackageManager.GET_SIGNATURES
                    or PackageManager.GET_ACTIVITIES
                    or PackageManager.GET_META_DATA
                    or PackageManager.GET_RECEIVERS
                    or PackageManager.GET_SERVICES
                    or PackageManager.GET_PROVIDERS
        )
    }


    private object VersionCompat {
        val MATCH_UNINSTALLED_PACKAGES = if (atLeastN()) {
            PackageManager.MATCH_UNINSTALLED_PACKAGES
        } else {
            PackageManager.GET_UNINSTALLED_PACKAGES
        }

        fun atLeastN(): Boolean {
            return Build.VERSION.SDK_INT >= 24
        }
    }
}