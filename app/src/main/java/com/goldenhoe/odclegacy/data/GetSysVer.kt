package com.goldenhoe.odclegacy.data

import android.os.Build

object GetSysVer {
    /**
     * 获取 Android SDK 版本号
     */
    fun getSdkInt(): Int {
        return Build.VERSION.SDK_INT
    }

    /**
     * 判断当前系统版本是否高于或等于 Android 6.0 (API 23)
     * 如果返回 true，表示 >= Android 6.0，应显示推荐卡片
     * 如果返回 false，表示 Android 5.x (API 21-22)，应隐藏推荐卡片
     */
    fun shouldShowRecommendation(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
    }
}
