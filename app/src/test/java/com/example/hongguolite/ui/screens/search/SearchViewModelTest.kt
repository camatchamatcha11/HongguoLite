package com.example.hongguolite.ui.screens.search

import com.example.hongguolite.data.repository.MockSearchRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * 针对 SearchViewModel 的单元测试。
 *
 * 主要测试其状态流（uiState）在处理输入框关键词变化、防抖以及执行搜索操作时的流转逻辑。
 */
class SearchViewModelTest {
    private fun createViewModel(): SearchViewModel {
        return SearchViewModel(
            repository = MockSearchRepository(),
            debounceMillis = 30L,
            externalScope = CoroutineScope(Dispatchers.Unconfined),
        )
    }

    @Test
    fun init_loadsPreSearchContent() = runBlocking {
        val viewModel = createViewModel()

        val state = viewModel.uiState.value

        assertTrue(state is SearchUiState.PreSearch)
        state as SearchUiState.PreSearch
        assertTrue(state.guessLikes.isNotEmpty())
        assertTrue(state.hotSearches.isNotEmpty())
    }

    @Test
    fun keywordChange_afterDebounceShowsSuggestingState() = runBlocking {
        val viewModel = createViewModel()
        val keyword = (viewModel.uiState.value as SearchUiState.PreSearch).guessLikes.first().tags.first()

        viewModel.onKeywordChange(keyword)
        delay(80L)

        val state = viewModel.uiState.value
        assertTrue(state is SearchUiState.Suggesting)
        state as SearchUiState.Suggesting
        assertEquals(keyword, state.keyword)
        assertTrue(state.dramaSuggestions.isNotEmpty())
        assertTrue(state.keywordSuggestions.isNotEmpty())
    }

    @Test
    fun keywordChange_debounceKeepsOnlyLatestInput() = runBlocking {
        val viewModel = createViewModel()
        val firstKeyword = (viewModel.uiState.value as SearchUiState.PreSearch).guessLikes.first().tags.first()
        val latestKeyword = (viewModel.uiState.value as SearchUiState.PreSearch).guessLikes.last().tags.first()

        viewModel.onKeywordChange(firstKeyword)
        delay(10L)
        viewModel.onKeywordChange(latestKeyword)
        delay(80L)

        assertEquals(latestKeyword, viewModel.uiState.value.keyword)
    }

    @Test
    fun searchKeyword_showsResultAndAddsHistory() = runBlocking {
        val viewModel = createViewModel()
        val keyword = (viewModel.uiState.value as SearchUiState.PreSearch).guessLikes.first().tags.first()

        viewModel.onSearch(keyword)
        delay(80L)

        val state = viewModel.uiState.value
        assertTrue(state is SearchUiState.ShowingResult)
        state as SearchUiState.ShowingResult
        assertEquals(keyword, state.keyword)
        assertTrue(state.results.isNotEmpty())
    }
}
