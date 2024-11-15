package com.fzp.removedeadfrienddemo

import android.content.ActivityNotFoundException
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

class CheckFriendActivity : AppCompatActivity() {
    private var beginCheck: Button?=null
    private var recycleview:RecyclerView?=null
    private val TAG:String = "CheckFriendActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_check_friend)

        initView()
    }

    private fun initView() {
        beginCheck =findViewById(R.id.begin_check)
        recycleview =findViewById(R.id.recycleview)

        beginCheck?.setOnClickListener {
            startApp("com.tencent.mm", "com.tencent.mm.ui.LauncherUI", "未安装微信")
        }
    }

    /**
     * 跳转其它APP
     * @param packageName 跳转APP包名
     * @param activityName 跳转APP的Activity名
     * @param errorTips 跳转页面不存在时的提示
     * */
    fun Context.startApp(packageName: String, activityName: String, errorTips: String) {
        try {
            startActivity(Intent(Intent.ACTION_VIEW).apply {
                component = ComponentName(packageName, activityName)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            })
        } catch (e: ActivityNotFoundException) {
//            shortToast(errorTips)
            Toast.makeText(this, errorTips, Toast.LENGTH_SHORT).show();
        } catch (e: Exception) {
            e.message?.let { Log.e(TAG, "startApp: $it") }
        }
    }

    /**
     * 跳转其它APP
     * @param urlScheme URL Scheme请求字符串
     * @param errorTips 跳转页面不存在时的提示
     * */
    fun Context.startApp(urlScheme: String, errorTips: String) {
        try {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(urlScheme)))
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(this, errorTips, Toast.LENGTH_SHORT).show();
        } catch (e: Exception) {
            e.message?.let { Log.e(TAG, "startApp: $it") }
        }
    }

}