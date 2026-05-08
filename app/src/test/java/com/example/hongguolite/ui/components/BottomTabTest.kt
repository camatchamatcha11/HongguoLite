package com.example.hongguolite.ui.components

import com.example.hongguolite.navigation.Routes
import org.junit.Assert.assertEquals
import org.junit.Test

class BottomTabTest {
    @Test
    fun bottomTabs_matchMainAppTabOrder() {
        val routeAndLabel = bottomTabs.map { it.route to it.label }

        assertEquals(
            listOf(
                Routes.HOME to "首页",
                Routes.THEATER to "剧场",
                Routes.SHOP to "商城",
                Routes.EARN to "赚钱",
                Routes.PROFILE to "我的",
            ),
            routeAndLabel,
        )
    }
}
