package com.goldenhoe.odclegacy

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.NavType
import androidx.compose.runtime.remember
import androidx.compose.runtime.collectAsState
import com.goldenhoe.odclegacy.data.SettingsRepository
import com.goldenhoe.odclegacy.network.LlmApiService
import com.goldenhoe.odclegacy.ui.ChatScreen
import com.goldenhoe.odclegacy.ui.ModelEditScreen
import com.goldenhoe.odclegacy.ui.ModelSettingsScreen
import com.goldenhoe.odclegacy.ui.AboutScreen
// 请根据您的主题文件 (ui.theme/Theme.kt) 中定义的函数名修改此处的引用
import com.goldenhoe.odclegacy.ui.theme.OpenDroidChatTheme
import com.goldenhoe.odclegacy.viewmodel.ChatViewModel
import com.goldenhoe.odclegacy.viewmodel.ChatViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // 请将 OpenDroidChatTheme 替换为您主题文件中的实际函数名
            OpenDroidChatTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    MainNavigation()
                }
            }
        }
    }
}

// ------------------- Navigation Destinations -------------------

object Destinations {
    const val CHAT_SCREEN = "chat"
    const val MODEL_SETTINGS = "settings"
    const val MODEL_EDIT = "edit_model/{modelId}"
    const val ABOUT_SCREEN = "about"
    const val ARG_MODEL_ID = "modelId"
}

@Composable
fun MainNavigation() {
    val navController = rememberNavController()
    val context = LocalContext.current

    val viewModel: ChatViewModel = viewModel(factory = ChatViewModelFactory(context))

    val screens = listOf(
        Destinations.CHAT_SCREEN to "聊天",
        Destinations.MODEL_SETTINGS to "模型",
        Destinations.ABOUT_SCREEN to "关于"
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val bottomBar: @Composable (NavController) -> Unit = { navController ->
        // 检查当前路由是否是底部导航栏的路由之一
        val shouldShowBottomBar = currentDestination?.route in screens.map { it.first }
        if (shouldShowBottomBar) {
            NavigationBar {
                screens.forEach { (route, label) ->
                    NavigationBarItem(
                        icon = {
                            when (route) {
                                Destinations.CHAT_SCREEN -> Icon(Icons.Filled.Home, contentDescription = label)
                                Destinations.MODEL_SETTINGS -> Icon(Icons.Filled.List, contentDescription = label)
                                Destinations.ABOUT_SCREEN -> Icon(Icons.Filled.Info, contentDescription = label)
                                else -> Icon(Icons.Filled.MoreVert, contentDescription = label) // Fallback icon
                            }
                        },
                        label = { Text(label) },
                        selected = currentDestination?.hierarchy?.any { it.route == route } == true,
                        onClick = {
                            navController.navigate(route) {
                                // Pop up to the start destination of the graph to avoid building up a large stack
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                // Avoid multiple copies of the same destination when re-selecting the same item
                                launchSingleTop = true
                                // Restore state when re-selecting a previously selected item
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    }

    Scaffold(
        bottomBar = { bottomBar(navController) }
    ) { innerPadding ->
        NavHost(
            navController = navController as NavHostController,
            startDestination = Destinations.CHAT_SCREEN,
            modifier = Modifier.padding(innerPadding)
        ) {
            // 1. 聊天界面
            composable(Destinations.CHAT_SCREEN) {
                ChatScreen(
                    viewModel = viewModel,
                    onNavigateToSettings = { navController.navigate(Destinations.ABOUT_SCREEN) }
                )
            }

            // 2. 模型管理列表
            composable(Destinations.MODEL_SETTINGS) {
                ModelSettingsScreen(
                    viewModel = viewModel,
                    onBack = { navController.popBackStack() },
                    onNavigateToEditModel = { modelId ->
                        navController.navigate("edit_model/$modelId")
                    }
                )
            }

            // 3. 模型编辑/新增界面 (包含 Unresolved reference 的修复)
            composable(
                Destinations.MODEL_EDIT,
                arguments = listOf(navArgument(Destinations.ARG_MODEL_ID) {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null
                })
            ) { backStackEntry ->
                val modelId = backStackEntry.arguments?.getString(Destinations.ARG_MODEL_ID)

                // 修复后的代码：
                val allModelsList by viewModel.allModels.collectAsState()

                val modelToEdit = remember(modelId) {
                    allModelsList.find { it.id == modelId }
                }

                ModelEditScreen(
                    viewModel = viewModel,
                    modelToEdit = modelToEdit, // 直接传递找到的模型对象
                    onSave = { navController.popBackStack() }
                )
            }

            // 4. 关于界面
            composable(Destinations.ABOUT_SCREEN) {
                AboutScreen(
                    onBack = { navController.popBackStack() }
                )
            }
        }
    }
}
