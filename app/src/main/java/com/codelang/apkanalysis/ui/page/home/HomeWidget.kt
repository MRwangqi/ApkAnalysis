package com.codelang.apkanalysis.ui.page.home

import android.widget.ImageView
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavHostController
import com.codelang.apkanalysis.bean.ApkInfo
import com.codelang.apkanalysis.ui.page.Router
import com.codelang.apkanalysis.viewmodel.ApkViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeWidget(
    navCtrl: NavHostController,
    viewModel: ApkViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val apkInfos = viewModel.apkInfos
    DisposableEffect(Unit) {
        viewModel.dispatchData()
        onDispose {
        }
    }
    var searchText by remember {
        mutableStateOf("")
    }
    Scaffold(topBar = {
        SearchWidget(searchText) {
            searchText = it
            viewModel.apkInfos = viewModel.cacheData.filter {
                it.appName!!.contains(searchText) || it.packageName!!.contains(searchText)
            }.toList()
        }
    }) {
        if (apkInfos.isEmpty()) {
            if (viewModel.cacheData.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize()) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .size(50.dp)
                    )
                }
            } else {
                Box(modifier = Modifier.fillMaxSize()) {
                    Text(
                        text = "未搜到", modifier = Modifier
                            .align(Alignment.Center)
                    )
                }
            }
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                items(apkInfos, key = { apk -> apk.packageName!! }) { apk ->
                    AppInfoItem(apk) {
                        navCtrl.navigate(Router.DETAIL + "/" + apk.packageName)
                    }
                }
            }
        }
    }

}

@Composable
fun AppInfoItem(apk: ApkInfo, onClick: () -> Unit) {
    Card(modifier = Modifier.padding(10.dp, 5.dp, 10.dp, 0.dp) .background(MaterialTheme.colorScheme.background.copy(0.1f))) {
        Row(
            Modifier
                .fillMaxWidth()
                .clickable(onClick = onClick)
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
                Text(text = "版本号:" + apk.versionName+"("+apk.versionCode+")", fontSize = 11.sp, maxLines = 1)
                Text(text = "Target Api:" + apk.targetSdk, fontSize = 11.sp, maxLines = 1)
            }
        }
    }

}


@Composable
fun SearchWidget(value: String, onValueChange: (String) -> Unit) {
    var showHint by remember {
        mutableStateOf(true)
    }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primary)
            .height(45.dp)

    ) {
        BasicTextField(
            singleLine = true,
            value = value,
            onValueChange = {
                showHint = it.isEmpty()
                onValueChange.invoke(it)
            },
            modifier = Modifier
                .padding(5.dp)
                .fillMaxSize(),
            decorationBox = { innerTextField ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(horizontal = 10.dp)
                        .fillMaxSize()
                        .clip(RoundedCornerShape(35.dp))
                ) {
                    Icon(imageVector = Icons.Filled.Search, contentDescription = "搜索")
                    Spacer(modifier = Modifier.width(10.dp))
                    Box(
                        modifier = Modifier.weight(1f),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        if (showHint) {
                            Text(text = "搜索")
                        }
                        innerTextField()
                    }
                }
            }
        )
    }
}


