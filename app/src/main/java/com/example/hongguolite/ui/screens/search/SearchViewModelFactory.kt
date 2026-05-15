package com.example.hongguolite.ui.screens.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.hongguolite.data.repository.MockSearchRepository

/**
 * 简易依赖注入入口。
 *
 * 这个项目暂时不用 Hilt，所以由 Factory 负责把 Repository 传给 ViewModel。
 * Module 5/3.7 替换数据源时，优先改这里，而不是到 UI 里到处 new Repository。
 */
class SearchViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SearchViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SearchViewModel(MockSearchRepository()) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}
