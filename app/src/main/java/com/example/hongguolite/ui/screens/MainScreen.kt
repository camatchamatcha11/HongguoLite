package com.example.hongguolite.ui.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.hongguolite.navigation.Routes
import com.example.hongguolite.ui.components.bottomTabs

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.CurrencyExchange
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.ui.graphics.Color
import com.example.hongguolite.ui.theme.HongguoRed
import com.example.hongguolite.ui.theme.TabUnselectedGray

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
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = HongguoRed,
                            selectedTextColor = HongguoRed,
                            unselectedIconColor = TabUnselectedGray,
                            unselectedTextColor = TabUnselectedGray,
                            indicatorColor = Color.Transparent // Optional: remove the background pill if desired, or let it be default
                        ),
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
            startDestination = Routes.HOME,  // 默认起始页：首页
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            composable(Routes.HOME) {
                HomeScreen(
                    onSearchClick = {
                        // 暂时弹个 Toast，搜索页 Module 3 才实现
                        // 等 Module 3 改成 navController.navigate(Routes.SEARCH)
                    }
                )
            }
            composable(Routes.THEATER) {
                PlaceholderScreen(icon = Icons.Default.PlayArrow, title = "剧场页面")
            }
            composable(Routes.SHOP) {
                PlaceholderScreen(icon = Icons.Default.ShoppingCart, title = "商城页面")
            }
            composable(Routes.EARN) {
                PlaceholderScreen(icon = Icons.Default.CurrencyExchange, title = "赚钱页面")
            }
            composable(Routes.PROFILE) {
                PlaceholderScreen(icon = Icons.Default.AccountCircle, title = "我的页面")
            }
        }
    }
}

