package com.codelang.apkanalysis.ui.page.detail.page

import android.widget.ImageView
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.codelang.apkanalysis.bean.ApkInfo
import com.codelang.apkanalysis.ui.page.home.AppInfoItem
import com.codelang.apkanalysis.viewmodel.AppViewAction

/**
 * @author wangqi
 * @since 2022/5/27.
 */


@Composable
fun AppDetailWidget(data:()-> ApkInfo?) {
    val apkInfo = data()
    apkInfo?.let { apk ->

        Row(
            Modifier
                .fillMaxSize()
                .padding(5.dp)
        ) {
            AndroidView(modifier = Modifier.size(50.dp), factory = {
                ImageView(it)
            }, update = {
                it.setImageDrawable(apk.icon)
            })
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(
                    text = apk.appName ?: "",
                    fontSize = 15.sp,
                    style = MaterialTheme.typography.titleMedium, maxLines = 1
                )
                Text(text = "包名:" + apk.packageName, fontSize = 13.sp, maxLines = 1)
                Text(
                    text = "版本号:" + apk.versionName + "(" + apk.versionCode + ")",
                    fontSize = 11.sp,
                    maxLines = 1
                )
                Text(
                    text = "targetSdkVersion " + apk.targetSdk + " minSdkVersion " + apk.minSdk,
                    fontSize = 11.sp,
                    maxLines = 1
                )
                Text(text = "首次安装时间:" + apk.firstInstallTime, fontSize = 11.sp, maxLines = 1)
                Text(text = "最后更新时间:" + apk.lastUpdateTime, fontSize = 11.sp, maxLines = 1)

                Text(text = "SHA1:", fontSize = 13.sp)
                SelectionContainer {
                    Text(
                        text = apk.SHA1 ?: "",
                        style = TextStyle.Default.copy(color = Color.Blue),
                        fontSize = 13.sp
                    )
                }
                Text(text = "SHA-256:", fontSize = 13.sp)
                SelectionContainer {
                    Text(
                        text = apk.SHA256 ?: "",
                        style = TextStyle.Default.copy(color = Color.Blue),
                        fontSize = 13.sp
                    )
                }
                Text(text = "MD5:", fontSize = 13.sp)
                SelectionContainer {
                    Text(
                        text = apk.MD5 ?: "",
                        style = TextStyle.Default.copy(color = Color.Blue),
                        fontSize = 13.sp
                    )
                }
            }
        }
    }

}