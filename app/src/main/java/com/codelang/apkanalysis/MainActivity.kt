package com.codelang.apkanalysis

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.codelang.apkanalysis.bean.ApkInfo
import com.codelang.apkanalysis.ui.page.AppWidget
import com.codelang.apkanalysis.ui.page.NestedWidget
import com.codelang.apkanalysis.ui.theme.ApkAnalysisTheme
import com.codelang.apkanalysis.viewmodel.ApkViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        composePage()
        nativePage()
    }


    private fun composePage() {
        setContent {
            ApkAnalysisTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppWidget()
                }
            }
        }
    }


   private val viewModel by viewModels<ApkViewModel>()
    private fun nativePage() {
        setContentView(R.layout.layout_main)
        val adapter = AppListAdapter()

        val rv = findViewById<RecyclerView>(R.id.rv)
        rv.layoutManager = LinearLayoutManager(this)
        rv.adapter = adapter

        viewModel.apkList.observe(this){
            adapter.submitList(it)
        }
        // todo 请求
        viewModel.dispatchData()
    }
}


class AppListAdapter() : ListAdapter<ApkInfo, AppListAdapter.AppListViewHolder>(AppListDiff) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_app, parent, false)
        return AppListViewHolder(view)
    }

    override fun onBindViewHolder(holder: AppListViewHolder, position: Int) {
        val apk = getItem(position)

        holder.run {
            iconImage.setImageDrawable(apk.icon)
            tvAppName.text = apk.appName ?: ""

            tvAppPackage.text = "包名:" + apk.packageName
            tvAppVersion.text ="版本号:" + apk.versionName+"("+apk.versionCode+")"
            tvAppTarget.text = "Target Api:" + apk.targetSdk
        }
    }


    private object AppListDiff : DiffUtil.ItemCallback<ApkInfo>() {
        override fun areItemsTheSame(oldItem: ApkInfo, newItem: ApkInfo): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: ApkInfo, newItem: ApkInfo): Boolean {
            return oldItem.packageName == newItem.packageName
        }
    }

    class AppListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val iconImage = itemView.findViewById<ImageView>(R.id.iconImage)
        val tvAppName = itemView.findViewById<TextView>(R.id.tvAppName)
        val tvAppPackage = itemView.findViewById<TextView>(R.id.tvAppPackage)
        val tvAppVersion = itemView.findViewById<TextView>(R.id.tvAppVersion)
        val tvAppTarget = itemView.findViewById<TextView>(R.id.tvAppTarget)
    }
}

