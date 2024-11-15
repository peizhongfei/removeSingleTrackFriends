package com.fzp.removedeadfrienddemo

import android.content.Intent
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Button
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    private var openIt:Button?=null
    private var beginCheck:Button?=null
    private var tvShow:TextView?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initView()
    }

    private fun initView() {
        openIt=findViewById(R.id.open_it)
        beginCheck=findViewById(R.id.begin_check)
        tvShow=findViewById(R.id.tv)

        openIt?.setOnClickListener { openAccbilityService() }
        beginCheck?.setOnClickListener { startActivity(Intent(this,CheckFriendActivity::class.java)) }
    }

    private fun openAccbilityService() {
        if (!RemoveFriendService.isServiceAvaible) {
            startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
        }
    }

    override fun onResume() {
        super.onResume()
        refreshStatus()
    }

    private fun refreshStatus() {
        if (RemoveFriendService.isServiceAvaible) {
            tvShow?.text = "移除僵尸好友服务状态：已开启"
            openIt?.visibility = View.INVISIBLE
            beginCheck?.visibility = View.VISIBLE
        }else{
            tvShow?.text = "移除僵尸好友服务状态：未开启"

            openIt?.visibility = View.VISIBLE
            beginCheck?.visibility = View.INVISIBLE
        }
    }
}