package com.codelang.apkanalysis.viewmodel

import android.app.Application
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.codelang.apkanalysis.bean.ApkInfo
import com.codelang.apkanalysis.bean.toApkInfo
import com.codelang.apkanalysis.bean.toTime
import com.codelang.apkanalysis.ext.toFileSize
import com.codelang.apkanalysis.utils.PackageUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jf.dexlib2.DexFileFactory
import org.jf.dexlib2.Opcodes
import java.io.BufferedInputStream
import java.io.File
import java.io.FileInputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream

class DetailViewModel(app: Application) : AndroidViewModel(app) {

    var appState by mutableStateOf(AppViewState())


    fun dispatch(params: AppParams, action: AppViewAction) {
        when (action) {
            AppViewAction.ApkDetailAction -> {
                getApkInfo(params)
            }
            AppViewAction.SoLibraryAction -> {
                getSoLibList(params)
            }
            AppViewAction.ActivityAction -> {
                getActivityList(params)
            }
            AppViewAction.BroadcastAction -> {
                getBroadcast(params)
            }
            AppViewAction.PermissionAction -> {
                getPermissionList(params)
            }
            AppViewAction.ProviderAction -> {
                getProviderList(params)
            }
            AppViewAction.ServiceAction -> {
                getServiceList(params)
            }
            AppViewAction.MetaAction -> {
                getMetaList(params)
            }
            AppViewAction.DexAction -> {
                getDexAndManifestList(params)
            }
        }
    }


    private fun getApkInfo(params: AppParams) {
        val packageInfo = PackageUtils.getPackageInfo(params.packageName)
        val apkInfo = packageInfo.toApkInfo()
        appState = appState.copy(apkInfo = apkInfo)

        //todo 分析 apk
        analysisApk(apkInfo.apkPath ?: "")
    }


    private fun getSoLibList(params: AppParams) {
        val packageInfo = PackageUtils.getPackageInfo(params.packageName)
        val apkInfo = packageInfo.toApkInfo()
        val soLibList =
            File(apkInfo.nativeLibraryDir).listFiles()
                ?.asSequence()
                ?.map { ItemData(it.name, it.length()) }
                ?.sortedByDescending { it.size }
                ?.toList()
                ?: arrayListOf()
        appState = appState.copy(soLibList = soLibList)
    }


    private fun getActivityList(params: AppParams) {
        val packageInfo = PackageUtils.getPackageInfo(params.packageName)
        val activityList =
            packageInfo.activities
                ?.asSequence()
                ?.map { ItemData(it.name) }
                ?.sortedBy { it.title }
                ?.toList()
                ?: arrayListOf()
        appState = appState.copy(activityList = activityList)
    }


    private fun getServiceList(params: AppParams) {
        val packageInfo = PackageUtils.getPackageInfo(params.packageName)
        // service
        val serviceList =
            packageInfo.services
                ?.asSequence()
                ?.map { ItemData(it.name) }
                ?.sortedBy { it.title }
                ?.toList() ?: arrayListOf()
        appState = appState.copy(serviceList = serviceList)
    }

    private fun getBroadcast(params: AppParams) {
        val packageInfo = PackageUtils.getPackageInfo(params.packageName)
        val broadcastList =
            packageInfo.receivers
                ?.asSequence()
                ?.map { ItemData(it.name) }
                ?.sortedBy { it.title }
                ?.toList()
                ?: arrayListOf()
        appState = appState.copy(broadcastList = broadcastList)
    }

    private fun getProviderList(params: AppParams) {
        val packageInfo = PackageUtils.getPackageInfo(params.packageName)
        val providerList =
            packageInfo.providers
                ?.asSequence()
                ?.map { ItemData(it.name) }
                ?.sortedBy { it.title }
                ?.toList()
                ?: arrayListOf()
        appState = appState.copy(providerList = providerList)
    }

    private fun getPermissionList(params: AppParams) {
        val packageInfo = PackageUtils.getPackageInfo(params.packageName)
        val permissionList =
            packageInfo.requestedPermissions
                ?.asSequence()
                ?.map { ItemData(it) }
                ?.sortedBy { it.title }
                ?.toList()
                ?: arrayListOf()
        appState = appState.copy(permissionList = permissionList)
    }

    private fun getMetaList(params: AppParams) {
        val packageInfo = PackageUtils.getPackageInfo(params.packageName)
        val metaList =
            packageInfo.applicationInfo.metaData?.let { bundle ->
                bundle.keySet().map {
                    ItemData(it, subTitle = bundle.get(it).toString())
                }.sortedBy { it.title }.toList()
            } ?: arrayListOf()

        appState = appState.copy(metaList = metaList)
    }


    private fun getDexAndManifestList(params: AppParams) {
        viewModelScope.launch {
            val dexList = withContext(Dispatchers.IO) {
                val apkPath =
                    PackageUtils.getPackageInfo(params.packageName).applicationInfo.publicSourceDir
                val dexList =
                    DexFileFactory.loadDexFile(
                        File(apkPath),
                        Opcodes.getDefault()
                    )
                val pkgType = "L" + params.packageName.replace(".", "/")

                dexList.classes.asSequence().filter {
                    // 当前包名的不要
                    !it.type.startsWith(pkgType)
                }.map {
                    // 转成类名
                    it.type.substring(1, it.length - 1).replace("/", ".")
                }.groupBy {
                    // 取类名的前两个包作为 pkg 分组
                    it.split(".").take(2).joinToString(".")
                }.map {
                    ItemData(it.key)
                }.toList()
            }
            appState = appState.copy(dexList = dexList)
        }

    }


    private fun analysisApk(path: String) {
        var resSize = 0L
        var assetsSize = 0L
        var dexSize = 0L
        var arscSize = 0L
        var libSize = 0L
        var otherSize = 0L

        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                kotlin.runCatching {
                    val input = FileInputStream(path)
                    val zipInputStream = ZipInputStream(BufferedInputStream(input))
                    var ze: ZipEntry?
                    while (zipInputStream.nextEntry.also { ze = it } != null) {
                        if (ze!!.name.startsWith("res/")) {
                            resSize += ze!!.size
                        } else if (ze!!.name.startsWith("assets/")) {
                            assetsSize += ze!!.size
                        } else if (ze!!.name.endsWith(".dex")) {
                            dexSize += ze!!.size
                        } else if (ze!!.name == "resources.arsc") {
                            arscSize += ze!!.size
                        } else if (ze!!.name.startsWith("lib/") || ze!!.name.startsWith("libs/")) {
                            libSize += ze!!.size
                        } else {
                            otherSize += ze!!.size
                        }
                    }
                    zipInputStream.closeEntry()
                    input.close()
                }.onFailure {
                    Log.e("analysisApk", "throwable" + it.message)
                }
            }

            val apkAnalysis =  arrayListOf<Pair<ApkType, Long>>()
            apkAnalysis.add(Pair(ApkType.RES,resSize))
            apkAnalysis.add(Pair(ApkType.ASSETS,assetsSize))
            apkAnalysis.add(Pair(ApkType.DEX,dexSize))
            apkAnalysis.add(Pair(ApkType.ARSC,arscSize))
            apkAnalysis.add(Pair(ApkType.LIB,libSize))
            apkAnalysis.add(Pair(ApkType.OTHER,otherSize))
            appState = appState.copy(apkAnalysis = apkAnalysis)
        }
    }
}

enum class ApkType {
    RES,
    ASSETS,
    DEX,
    ARSC,
    LIB,
    OTHER
}

data class AppParams(var packageName: String)

data class AppViewState(
    var apkInfo: ApkInfo? = null,
    var apkAnalysis: List<Pair<ApkType, Long>> = arrayListOf(),
    var soLibList: List<ItemData>? = null,
    var activityList: List<ItemData>? = null,
    var serviceList: List<ItemData>? = null,
    var broadcastList: List<ItemData>? = null,
    var providerList: List<ItemData>? = null,
    var permissionList: List<ItemData>? = null,
    var metaList: List<ItemData>? = null,
    var dexList: List<ItemData>? = null
)

data class ItemData(
    var title: String = "",
    var size: Long = 0,
    var subTitle: String = "",
    var icon: String = ""
)

sealed class AppViewAction {
    object ApkDetailAction : AppViewAction()
    object SoLibraryAction : AppViewAction()
    object ActivityAction : AppViewAction()
    object ServiceAction : AppViewAction()
    object BroadcastAction : AppViewAction()
    object ProviderAction : AppViewAction()
    object PermissionAction : AppViewAction()
    object MetaAction : AppViewAction()
    object DexAction : AppViewAction()
}