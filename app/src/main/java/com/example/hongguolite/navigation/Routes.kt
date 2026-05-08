package com.example.hongguolite.navigation

// 用 object 定义路由常量。新人坑：不要用魔法字符串散落在代码各处。
// object定义单例类，可直接访问其中常量，等价于public final class Routes
object Routes {
    const val HOME = "home"
    const val THEATER = "theater"
    const val SHOP = "shop"
    const val EARN = "earn"
    const val PROFILE = "profile"
}