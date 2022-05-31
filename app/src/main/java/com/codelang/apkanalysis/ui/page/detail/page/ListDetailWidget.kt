package com.codelang.apkanalysis.ui.page.detail.page

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.codelang.apkanalysis.R
import com.codelang.apkanalysis.ext.toFileSize
import com.codelang.apkanalysis.viewmodel.AppViewAction
import com.codelang.apkanalysis.viewmodel.ItemData

/**
 * @author wangqi
 * @since 2022/5/28.
 */


@Composable
fun ListDetailWidget(action: AppViewAction, data: () -> List<ItemData>?) {
    val list: List<ItemData>? = data()
    if (list == null) {
        CircularProgressIndicator(
            modifier = Modifier
                .size(50.dp)
        )
    } else if (list.isEmpty()) {
        Image(painter = painterResource(id = R.drawable.ic_empty_list), contentDescription = "空")
    } else {

        Column(
            Modifier
                .fillMaxSize()
                .padding(5.dp)
        ) {
            println("ListDetailWidget --> $action ")

            Text(text = "项数:(${list.size})", modifier = Modifier.height(30.dp))

            LazyColumn(
                Modifier
                    .fillMaxSize()
                    .padding(5.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {

                items(list) { item ->
                    Column(
                        modifier = Modifier
                            .padding(vertical = 5.dp)
                    ) {

                        Text(text = item.title, fontSize = 13.sp)

                        if (action == AppViewAction.SoLibraryAction) {
                            Text(
                                text = "(" + item.size.toFileSize() + ")",
                                fontSize = 11.sp,

                                )
                        } else if (action == AppViewAction.MetaAction) {
                            Text(
                                text = item.subTitle,
                                fontSize = 11.sp
                            )
                        }

                        Spacer(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(1.dp)
                                .background(MaterialTheme.colorScheme.primary.copy(0.5f))
                        )
                    }
                }
            }
        }
    }
}
