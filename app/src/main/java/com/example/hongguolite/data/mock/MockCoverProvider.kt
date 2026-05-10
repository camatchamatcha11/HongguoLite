package com.example.hongguolite.data.mock
import com.example.hongguolite.R
// 饿汉式单例对象
object MockCoverProvider {
    private val  placeholders = listOf(
        R.drawable.cover_placeholder_1,
        R.drawable.cover_placeholder_2,
        R.drawable.cover_placeholder_3,
        R.drawable.cover_placeholder_4,
        R.drawable.cover_placeholder_5,
    )

    /** 根据剧目 id 稳定地返回一张占位图（同一个 id 总是返回同一张） */
    fun getPlaceholder(dramaId: String): Int {
        // 用 id 的 hashCode 取模，保证同一 drama 总是同一张图，不会闪
        val index = (dramaId.hashCode() % placeholders.size + placeholders.size) % placeholders.size
        return placeholders[index]
    }
}