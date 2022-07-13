package com.codelang.apkanalysis.viewmodel

import android.app.Application
import android.content.pm.ApplicationInfo
import android.os.Build
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.codelang.apkanalysis.bean.ApkInfo
import com.codelang.apkanalysis.bean.toApkInfo
import com.codelang.apkanalysis.utils.PackageUtils
import com.codelang.apkanalysis.utils.SignType
import com.codelang.apkanalysis.utils.SigningUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.BufferedInputStream
import java.io.FileInputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream


class ApkViewModel(app: Application) : AndroidViewModel(app) {
    var apkInfos by mutableStateOf(listOf<ApkInfo>())
    var cacheData = listOf<ApkInfo>()


    var apkList  = MutableLiveData<List<ApkInfo>>()

    fun dispatchData() {
        viewModelScope.launch {
            apkInfos = withContext(Dispatchers.IO) {
                PackageUtils.getInstallApplications()
                    .filter {
                        ApplicationInfo.FLAG_SYSTEM and it.applicationInfo.flags == 0
                    }
                    .map { apk ->
                        apk.toApkInfo()
                    }.toList()
            }
            cacheData = apkInfos
            apkList.postValue(apkInfos)
        }
    }

}