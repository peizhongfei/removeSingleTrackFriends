package com.fzp.removedeadfrienddemo

import android.accessibilityservice.AccessibilityService
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityEvent.TYPE_VIEW_CLICKED
import android.view.accessibility.AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED
import android.view.accessibility.AccessibilityNodeInfo
import java.lang.Thread.sleep

/**
 *@author zhongfeiPei
 *@date on 2024/11/12 17:08
 *@email zhongfei.p@verifone.cn
 *@describe 无障碍辅助服务
 *
 */
class RemoveFriendService:AccessibilityService() {
     val CONTACT_LIST_ID = "js"
     val CONTACT_ITEM_ID = "hg4"

    companion object{
        var instances:RemoveFriendService?=null //单例
        val isServiceAvaible:Boolean get() = instances!=null //判断无障碍服务是否可用
        const val LAUNCHER_UI = "com.tencent.mm.ui.LauncherUI"  // 首页
        //   source.findAccessibilityNodeInfosByText("通讯录")[0].performAction(AccessibilityNodeInfo.ACTION_CLICK)
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        when (event?.eventType) {
            TYPE_WINDOW_STATE_CHANGED -> {
                when (event.className.toString()) {
                    LAUNCHER_UI -> {
                        event.source?.let { source -> source.getNodeByText("通讯录").click() }
                    }
                }
            }
            TYPE_VIEW_CLICKED -> {
                if (event.text[0] == "通讯录") {
                    // 这里不能用event的getSource()，只能获取到发生改变的节点
                    // 需要调用getRootInActiveWindow()获得所有结点
                    rootInActiveWindow?.let { source ->
                        val contactList = source.getNodeById(wxNodeId(CONTACT_LIST_ID))
                        if (contactList != null) {
                            contactList.getNodeById(wxNodeId(CONTACT_ITEM_ID)).click()
                        } else {
                            Log.d("TAG", "未能获取好友列表")
                        }
                    }

                }
            }
            else -> Log.d("TAG", "$event")
        }

    }

    private fun wxNodeId(contactItemId: String): String {
        return "com.tencent.mm:id/$contactItemId"
    }

    override fun onInterrupt() {

    }

    override fun onServiceConnected() {
        super.onServiceConnected()
        instances = this
    }

    override fun onDestroy() {
        super.onDestroy()
        instances = null
    }

    // 点击
    fun AccessibilityNodeInfo?.click() {
        if (this == null) return
        if (this.isClickable) {
            this.performAction(AccessibilityNodeInfo.ACTION_CLICK)
            return
        } else {
            this.parent.click()
        }
    }

    // 长按
    fun AccessibilityNodeInfo?.longClick() {
        if (this == null) return
        if (this.isClickable) {
            this.performAction(AccessibilityNodeInfo.ACTION_LONG_CLICK)
            return
        } else {
            this.parent.longClick()
        }
    }

    /**
     * 根据id查找单个节点
     * @param id 控件id
     * @return 对应id的节点
     * */
    fun AccessibilityNodeInfo.getNodeById(id: String): AccessibilityNodeInfo? {
        var count = 0
        while (count < 10) {
            findAccessibilityNodeInfosByViewId(id).let {
                if (!it.isNullOrEmpty()) return it[0]
            }
            sleep(100)
            count++
        }
        return null
    }

    /**
     * 根据id查找多个节点
     * @param id 控件id
     * @return 对应id的节点列表
     * */
    fun AccessibilityNodeInfo.getNodesById(id: String): List<AccessibilityNodeInfo>? {
        var count = 0
        while (count < 10) {
            findAccessibilityNodeInfosByViewId(id).let {
                if (!it.isNullOrEmpty()) return it
            }
            sleep(100)
            count++
        }
        return null
    }

    /**
     * 根据文本查找单个节点
     * @param text 匹配文本
     * @param allMatch 是否全匹配，默认false，contains()方式的匹配
     * @return 匹配文本的节点
     * */
    fun AccessibilityNodeInfo.getNodeByText(
        text: String,
        allMatch: Boolean = false
    ): AccessibilityNodeInfo? {
        var count = 0
        while (count < 10) {
            findAccessibilityNodeInfosByText(text).let {
                if (!it.isNullOrEmpty()) {
                    if (allMatch) {
                        it.forEach { node -> if (node.text == text) return node }
                    } else {
                        return it[0]
                    }
                }
                sleep(100)
                count++
            }
        }
        return null
    }

    /**
     * 根据文本查找多个节点
     * @param text 匹配文本
     * @param allMatch 是否全匹配，默认false，contains()方式的匹配
     * @return 匹配文本的节点列表
     * */
    fun AccessibilityNodeInfo.getNodesByText(
        text: String,
        allMatch: Boolean = false
    ): List<AccessibilityNodeInfo>? {
        var count = 0
        while (count < 10) {
            findAccessibilityNodeInfosByText(text).let {
                if (!it.isNullOrEmpty()) {
                    return if (allMatch) {
                        val tempList = arrayListOf<AccessibilityNodeInfo>()
                        it.forEach { node -> if (node.text == text) tempList.add(node) }
                        if (tempList.isEmpty()) null else tempList
                    } else {
                        it
                    }
                }
                sleep(100)
                count++
            }
        }
        return null
    }

    /**
     * 获取结点的文本
     * */
    fun AccessibilityNodeInfo?.text(): String {
        return this?.text?.toString() ?: ""
    }

}