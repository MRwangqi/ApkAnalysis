package com.codelang.apkanalysis.utils

import android.content.pm.PackageInfo
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

enum class SignType {
    SHA1, MD5, SHA256
}

object SigningUtils {
    /**
     * 获取 Signing
     * @param type 类型
     */
    fun getSign(type: SignType, info: PackageInfo): String? {
        try {
            val cert = info.signatures[0].toByteArray()
            val md = MessageDigest.getInstance(type.name)
            val publicKey = md.digest(cert)
            val hexString = StringBuffer()
            for (i in publicKey.indices) {
                val appendString = Integer.toHexString(
                    0xFF and publicKey[i]
                        .toInt()
                )
                    .uppercase()
                if (appendString.length == 1) hexString.append("0")
                hexString.append(appendString)
                hexString.append(":")
            }
            val result = hexString.toString()
            return result.substring(0, result.length - 1)
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        }
        return null
    }
}