package com.example.hongguolite.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.CurrencyExchange
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.hongguolite.navigation.Routes
import com.example.hongguolite.ui.components.bottomTabs
import com.example.hongguolite.ui.screens.HomeScreen
import com.example.hongguolite.ui.screens.PlaceholderScreen
import com.example.hongguolite.ui.screens.search.SearchScreen
import com.example.hongguolite.ui.screens.theater.TheaterScreen
import com.example.hongguolite.ui.theme.HongguoLiteTheme
import com.example.hongguolite.ui.theme.HongguoRed
import com.example.hongguolite.ui.theme.TabUnselectedGray

@Composable
fun MainScreen() {
    // NavController owns the app route back stack. Keeping it here makes bottom tabs and
    // non-tab pages share the same navigation graph.
    val navController = rememberNavController()
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route
    val shouldShowBottomBar = bottomTabs.any { it.route == currentRoute }

    Scaffold(
        bottomBar = {
            if (shouldShowBottomBar) {
                NavigationBar {
                    bottomTabs.forEach { tab ->
                        NavigationBarItem(
                            selected = currentRoute == tab.route,
                            onClick = {
                                navController.navigate(tab.route) {
                                    // Tab navigation should reuse the existing tab destinations instead of
                                    // stacking duplicate Home/Theater/Shop pages on every tap.
                                    popUpTo(navController.graph.startDestinationId) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = HongguoRed,
                                selectedTextColor = HongguoRed,
                                unselectedIconColor = TabUnselectedGray,
                                unselectedTextColor = TabUnselectedGray,
                                indicatorColor = Color.Transparent,
                            ),
                            icon = {
                                Icon(
                                    imageVector = tab.icon,
                                    contentDescription = tab.label,
                                )
                            },
                            label = {
                                Text(text = tab.label)
                            },
                        )
                    }
                }
            }
        },
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Routes.HOME,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            composable(Routes.HOME) {
                HomeScreen(
                    onSearchClick = {
                        navController.navigate(Routes.SEARCH)
                    },
                )
            }
            composable(Routes.THEATER) {
                TheaterScreen(
                    onSearchBoxClick = {
                        navController.navigate(Routes.SEARCH)
                    },
                    onRankClick = {
                        // Module 4 will replace this placeholder with the rank route.
                    },
                )
            }
            composable(Routes.SHOP) {
                PlaceholderScreen(icon = Icons.Default.ShoppingCart, title = "\u5546\u57ce\u9875\u9762")
            }
            composable(Routes.EARN) {
                PlaceholderScreen(icon = Icons.Default.CurrencyExchange, title = "\u8d5a\u94b1\u9875\u9762")
            }
            composable(Routes.PROFILE) {
                PlaceholderScreen(icon = Icons.Default.AccountCircle, title = "\u6211\u7684\u9875\u9762")
            }
            composable(Routes.SEARCH) {
                // Search is not a bottom tab page, so the bottom bar is hidden while this
                // destination is current.
                SearchScreen(
                    onBackClick = {
                        navController.popBackStack()
                    },
                )
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 393, heightDp = 852)
@Composable
fun MainScreenPreview() {
    HongguoLiteTheme(dynamicColor = false) {
        MainScreen()
    }
}
