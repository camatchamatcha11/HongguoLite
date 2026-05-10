package com.example.hongguolite.data.model

data class Drama (
    val id: String,           // 剧目唯一标识
    val title: String,        // 剧名（如"全球冰封：开局手握百亿物资"）
    val coverUrl: String,     // 封面图 URL（Module 5 用，本期暂用本地图占位）
    val heatCount: String,    // 热度（如 "4254万"）
    val tags: List<String>,   // 类型标签（如 ["科幻末世", "都市脑洞"]）
    val badge: DramaBadge? = null  // 角标（爆剧/新剧/null）
)


/**
 * 角标枚举
 *
 * sealed class 是 Kotlin 特色，比 enum 更灵活：
 * - enum 每项是固定值
 * - sealed class 每项可以是不同类型/带不同参数
 */
sealed class DramaBadge(val text: String, val colorHex: Long) {
    object HotDrama : DramaBadge("爆剧", 0xFFE6303C)   // 红色
    object NewDrama : DramaBadge("新剧", 0xFF34C759)   // 绿色
}