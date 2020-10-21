package com.zhkj.smartpolice

import android.app.Application
import android.content.Context
import android.text.TextUtils
import com.alibaba.android.arouter.launcher.ARouter
import com.sunny.zy.ZyFrameStore
import com.sunny.zy.utils.LogUtil
import com.umeng.commonsdk.UMConfigure
import com.umeng.message.IUmengRegisterCallback
import com.umeng.message.PushAgent
import com.umeng.message.UmengNotificationClickHandler
import com.umeng.message.entity.UMessage
import com.zhkj.smartpolice.notice.NoticeDetailActivity

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        ZyFrameStore.init(this)
        ARouter.init(this)
        ARouter.openDebug()
        ARouter.openLog()

        UMConfigure.init(
            this, "5f477b08c9f86377c197095e", "Umeng",
            UMConfigure.DEVICE_TYPE_PHONE, "30ec20f045b21d3dff4482687a20b1a2"
        )

        PushAgent.getInstance(this).register(object : IUmengRegisterCallback {
            override fun onSuccess(deviceToken: String?) {
                LogUtil.i("友盟注册成功!-token:$deviceToken")
            }

            override fun onFailure(s: String?, s1: String?) {
                LogUtil.i("友盟注册失败:s:$s - s1:$s1")
            }
        })

        val notificationClickHandler: UmengNotificationClickHandler = object : UmengNotificationClickHandler() {
            override fun openActivity(context: Context, msg: UMessage) {
                msg.activity?.let {
                    if (it.trim().isNotEmpty()){
                        if (it.contains("NoticeDetailActivity")){
                            NoticeDetailActivity.intent(context,msg.extra["noticeId"])
                        }
                    }
                }
                LogUtil.i("友盟:${msg.custom}")
            }
        }
        PushAgent.getInstance(this).notificationClickHandler = notificationClickHandler

    }
}