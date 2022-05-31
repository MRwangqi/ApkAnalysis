package com.codelang.apkanalysis.utils

import java.io.File
import java.io.InputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipFile

/**
 * @author wangqi
 * @since 2022/5/29.
 */
object ZipUtils {


    /**
     *  @param apkPath apk 路径
     *  @param zipFileName 压缩文件名
     */
    @JvmStatic
    fun getZipInput(apkPath: String, zipFileName: String): InputStream? {
        try {
            // 压缩文件路径和文件名
            val file = File(apkPath)
            val zipFile = ZipFile(file)
            // 所解压的文件名
            val entry: ZipEntry = zipFile.getEntry(zipFileName)
            return zipFile.getInputStream(entry)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }
}