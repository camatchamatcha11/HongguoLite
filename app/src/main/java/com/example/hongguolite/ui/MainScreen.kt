package com.example.hongguolite.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.hongguolite.navigation.Routes
import com.example.hongguolite.ui.components.bottomTabs

@Composable
fun MainScreen() {
    // 创建 NavController：rememberNavController 保证它在重组中保留
    val navController = rememberNavController()

    // 监听当前路由，用于决定 Tab 的选中态
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            NavigationBar {
                bottomTabs.forEach { tab ->
                    NavigationBarItem(
                        selected = currentRoute == tab.route,
                        onClick = {
                            navController.navigate(tab.route) {
                                // 回到主图的起点，避免 Tab 之间互相堆栈。
                                // 否则连续点 5 个 Tab 后，返回键会在 Tab 历史里来回退。
                                popUpTo(navController.graph.startDestinationId) {
                                    saveState = true
                                }

                                // 重复点击当前 Tab 时不再创建第二个相同页面。
                                launchSingleTop = true

                                // 切回之前访问过的 Tab 时，尽量恢复它自己的状态。
                                restoreState = true
                            }
                        },
                        icon = {
                            Icon(
                                imageVector = tab.icon,
                                contentDescription = tab.label
                            )
                        },
                        label = {
                            Text(text = tab.label)
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Routes.HOME,  // 默认起始页：首页；返回栈的根
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)  // 重要：避免内容被底部 Tab 遮挡
        ) {
            // TODO 4：定义每个路由对应的页面
            composable(Routes.HOME) {
                TabPlaceholderPage(title = "首页页面")
            }
            composable(Routes.THEATER) {
                TabPlaceholderPage(title = "剧场页面")
            }
            composable(Routes.SHOP) {
                TabPlaceholderPage(title = "商城页面")
            }
            composable(Routes.EARN) {
                TabPlaceholderPage(title = "赚钱页面")
            }
            composable(Routes.PROFILE) {
                TabPlaceholderPage(title = "我的页面")
            }
        }
    }
}

@Composable
private fun TabPlaceholderPage(title: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = title)
    }
}
