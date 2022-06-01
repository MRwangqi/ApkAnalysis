package com.codelang.apkanalysis.ui.page.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.codelang.apkanalysis.ui.page.detail.page.AppDetailWidget
import com.codelang.apkanalysis.ui.page.detail.page.ListDetailWidget
import com.codelang.apkanalysis.viewmodel.AppParams
import com.codelang.apkanalysis.viewmodel.AppViewAction
import com.codelang.apkanalysis.viewmodel.DetailViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@Composable
fun DetailWidget(
    navCtrl: NavHostController, packageName: String
) {
    Scaffold(topBar = {
        AppToolsBar(title = packageName, onBack = {
            navCtrl.navigateUp()
        })
    }) {
        TabViewPager(packageName)

//            AndroidView(modifier = Modifier.fillMaxSize(), factory = {
//                CodeView(it).apply {
//                    gravity = Gravity.TOP or Gravity.START
//                    this.setEnableLineNumber(true)
//                    this.setLineNumberTextColor(Color.GRAY)
//                    this.setLineNumberTextSize(25f)
//                    this.isFocusableInTouchMode = false
//                    this.setTabLength(4)
//                    this.setEnableAutoIndentation(true)
//                }
//            }, update = {
//                it.setText(xmlContent)
//            })
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun TabViewPager(
    packageName: String,
    viewModel: DetailViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val params = AppParams(packageName)

    val appState = viewModel.appState


    val tabData = listOf(
        "应用详情" to AppViewAction.ApkDetailAction,
        "原生库" to AppViewAction.SoLibraryAction,
        "服务" to AppViewAction.ServiceAction,
        "活动" to AppViewAction.ActivityAction,
        "广播接收器" to AppViewAction.BroadcastAction,
        "内容提供者" to AppViewAction.ProviderAction,
        "权限" to AppViewAction.PermissionAction,
        "元数据" to AppViewAction.MetaAction,
        "Dex" to AppViewAction.DexAction
    )


    val pagerState = rememberPagerState(
        initialPage = 0,
    )
    val tabIndex = pagerState.currentPage
    val coroutineScope = rememberCoroutineScope()


    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }
            .collect { page ->
                viewModel.dispatch(params, tabData[page].second)
            }
    }

    Column {

        ScrollableTabRow(
            edgePadding = 0.dp,
            selectedTabIndex = tabIndex,
            indicator = { positions ->
                TabRowDefaults.Indicator(
                    Modifier.tabIndicatorOffset(positions[tabIndex]),
                    height = 5.dp,
                    color = MaterialTheme.colorScheme.tertiary
                )
            },
        ) {
            tabData.forEachIndexed { index, action ->
                Tab(
                    modifier = Modifier.background(MaterialTheme.colorScheme.primary),
                    selected = tabIndex == index,
                    onClick = {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(index)
                        }
                    },
                    text = {
                        Text(text = action.first)
                    })
            }
        }

        HorizontalPager(
            key = { index ->
                tabData[index].first
            },
            count = tabData.size,
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { index ->
            when (val action = tabData[index].second) {
                AppViewAction.ApkDetailAction -> {
                    AppDetailWidget({ appState.apkInfo }, { appState.apkAnalysis })
                }
                AppViewAction.SoLibraryAction -> {
                    ListDetailWidget(action) { appState.soLibList }
                }
                AppViewAction.ActivityAction -> {
                    ListDetailWidget(action) { appState.activityList }
                }
                AppViewAction.BroadcastAction -> {
                    ListDetailWidget(action) { appState.broadcastList }
                }
                AppViewAction.PermissionAction -> {
                    ListDetailWidget(action) { appState.permissionList }
                }
                AppViewAction.ProviderAction -> {
                    ListDetailWidget(action) { appState.providerList }
                }
                AppViewAction.ServiceAction -> {
                    ListDetailWidget(action) { appState.serviceList }
                }
                AppViewAction.MetaAction -> {
                    ListDetailWidget(action) { appState.metaList }
                }
                AppViewAction.DexAction -> {
                    ListDetailWidget(action) { appState.dexList }
                }
            }
        }
    }
}


@Composable
fun AppToolsBar(
    title: String,
    onBack: (() -> Unit),
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primary)
            .height(45.dp)

    ) {
        Row(modifier = Modifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically) {
            Icon(
                Icons.Default.ArrowBack,
                null,
                Modifier
                    .clickable(onClick = onBack)
                    .align(Alignment.CenterVertically)
                    .padding(8.dp),
            )
            Spacer(modifier = Modifier.width(10.dp))

            Text(
                text = title,
                maxLines = 1
            )
        }
    }
}