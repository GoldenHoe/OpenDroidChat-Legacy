package com.goldenhoe.odclegacy.ui

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.InstallMobile
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import com.goldenhoe.odclegacy.data.GetSysVer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutScreen(
    onBack: () -> Unit = {}
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // 使用与主页相同的 TopAppBar 样式
        TopAppBar(
            title = { Text("关于", fontWeight = FontWeight.Normal) },
            windowInsets = WindowInsets(0, 0, 0, 0),
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // 版本说明
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "关于 OpenDroidChat-Legacy",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Text(
                        text = "OpenDroidChat-Legacy 是一款基于 OpenDroidChat 主线旧代码基修改的 LLM API 聊天客户端，适用于 Android 5(API24)+ ，UI界面使用了Google的Material Design 3设计框架。",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
            // 推荐使用 OpenDroidChat 主线（仅 Android 6.0+ 显示）
            if (GetSysVer.shouldShowRecommendation()) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "推荐使用 OpenDroidChat",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        Text(
                            text = "OpenDroidChat-Legacy 专为旧的Android系统用户提供安全支持弱的基础聊天服务，如需获得更强大的安全防护及更完善的功能，请使用 OpenDroidChat 主线项目",
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(bottom = 12.dp)
                        )
                        val context = LocalContext.current
                        Button(
                            onClick = {
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/HOE-Team/OpenDroidChat/releases/latest"))
                                context.startActivity(intent)
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(
                                imageVector = Icons.Filled.InstallMobile,
                                contentDescription = null,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("下载 OpenDroidChat")
                        }
                    }
                }
            }
            // 应用简介卡片
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "版本信息",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Text(
                        text="Legacy-release1" ,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text="* 你正在使用安全支持弱的 Legacy 分支，请注意 API Key 安全。",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Red,
                    )
                }
            }
            // 版权信息
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "版权信息",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Text(
                        text = "代码基：版权所有 ©2025 HOE Team ，保留所有权利。\n修改内容：版权所有 ©2026 GoldenHoe，保留所有权利\n代码基和修改源码均使用MIT协议开源",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}
