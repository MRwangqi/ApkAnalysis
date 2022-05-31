package com.codelang.apkanalysis.ui.page

import androidx.compose.foundation.background
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.codelang.apkanalysis.ui.page.detail.DetailWidget
import com.codelang.apkanalysis.ui.page.home.HomeWidget


object Router{
    const val HOME = "home"
    const val DETAIL = "detail"
}

@Composable
fun AppWidget(){
    val navCtrl = rememberNavController()

    NavHost(
        modifier = Modifier.background(MaterialTheme.colors.background),
        navController = navCtrl,
        startDestination = Router.HOME
    ) {
        //首页
        composable(route = Router.HOME) {
            HomeWidget(navCtrl)
        }

        //详情
        composable(route = Router.DETAIL+"/{packageName}",
            arguments = listOf(navArgument("packageName") { type = NavType.StringType })) {
            val apkInfo = it.arguments?.getString("packageName")?:""
            DetailWidget(navCtrl,apkInfo)
        }
    }
}