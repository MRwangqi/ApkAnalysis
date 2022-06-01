package com.codelang.apkanalysis.ui.page.detail.page

import android.widget.ImageView
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.toLowerCase
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.codelang.apkanalysis.bean.ApkInfo
import com.codelang.apkanalysis.ext.toFileSize
import com.codelang.apkanalysis.ui.widget.PieChartWidget
import com.codelang.apkanalysis.viewmodel.ApkType

/**
 * @author wangqi
 * @since 2022/5/27.
 */


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AppDetailWidget(data: () -> ApkInfo?, apkAnalysis: () -> List<Triple<ApkType, Long, Color>>) {
    val apkInfo = data()
    val analysis = apkAnalysis()
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

                if (analysis.isEmpty()) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(40.dp)
                            .align(Alignment.CenterHorizontally)
                    )
                } else {
                    PieChartApk(analysis)
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PieChartApk(analysis: List<Triple<ApkType, Long, Color>>) {
    val list = analysis.map { Pair(it.second, it.third) }.toList()

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        PieChartWidget(
            innerCircleColor = MaterialTheme.colorScheme.background,
            list = list,
            modifier = Modifier
                .size(80.dp)
        )

        LazyVerticalGrid(
            contentPadding = PaddingValues(10.dp),
            cells = GridCells.Adaptive(minSize = 120.dp)
        ) {
            items(analysis) { item ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Spacer(
                        modifier = Modifier
                            .size(10.dp)
                            .background(item.third)
                    )
                    Spacer(
                        modifier = Modifier
                            .width(3.dp)
                    )
                    Text(
                        text = "${item.first.toString().toLowerCase()}:${item.second.toFileSize()}",
                        fontSize = 11.sp,
                        maxLines = 1
                    )
                }
            }
        }
    }
}