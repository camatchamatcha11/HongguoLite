package com.example.hongguolite.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.hongguolite.navigation.Routes

// 底部 Tab 的单项配置：路由负责导航，label/icon 负责显示。
// 这样后续新增或调整 Tab 时，优先改配置列表，而不是散落修改 UI 代码。
data class BottomTabItem(
    val route: String,
    val label: String,
    val icon: ImageVector
)

// 红果主框架是 5 个底部 Tab。顺序要稳定，因为它影响用户的肌肉记忆和后续验收。
val bottomTabs = listOf(
    BottomTabItem(Routes.HOME, "首页", Icons.Default.Home),
    BottomTabItem(Routes.THEATER, "剧场", Icons.Default.Star),
    BottomTabItem(Routes.SHOP, "商城", Icons.Default.ShoppingCart),
    BottomTabItem(Routes.EARN, "赚钱", Icons.Default.AttachMoney),
    BottomTabItem(Routes.PROFILE, "我的", Icons.Default.Person),
)
