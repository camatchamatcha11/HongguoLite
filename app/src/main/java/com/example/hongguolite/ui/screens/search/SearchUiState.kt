package com.example.hongguolite.ui.screens.search

import com.example.hongguolite.data.model.Drama

/**
 * 搜索页的三态状态机。
 *
 * UI 只根据这个 sealed interface 做 when 分支，不需要自己猜“当前应该显示哪个页面”。
 * 后续 3.5、3.6 继续扩展中态和结果页时，也沿用这个状态入口。
 */
sealed interface SearchUiState {
    val keyword: String

    /**
     * 前置态：刚进入搜索页或清空输入框后展示。
     *
     * 这里放“猜你想搜、热搜榜、历史记录”，因为它们都不依赖当前输入过程。
     */
    data class PreSearch(
        override val keyword: String = "",
        val guessLikes: List<Drama> = emptyList(),
        val hotSearches: List<Drama> = emptyList(),
        val history: List<String> = emptyList(),
    ) : SearchUiState

    /**
     * 中态：用户正在输入，防抖完成后展示联想结果。
     *
     * dramaSuggestions 用于带封面的联想项，keywordSuggestions 用于普通文本联想项。
     */
    data class Suggesting(
        override val keyword: String,
        val dramaSuggestions: List<Drama> = emptyList(),
        val keywordSuggestions: List<String> = emptyList(),
    ) : SearchUiState

    /**
     * 结果态：用户点击“搜索”或选择联想词后展示。
     *
     * isLoading 先保留出来，后续接网络或更慢的数据源时 UI 能直接显示加载态。
     */
    data class ShowingResult(
        override val keyword: String,
        val results: List<Drama> = emptyList(),
        val isLoading: Boolean = false,
    ) : SearchUiState
}
