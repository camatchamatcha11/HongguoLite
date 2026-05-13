package com.example.hongguolite.data.repository

import com.example.hongguolite.data.mock.MockDramas
import com.example.hongguolite.data.model.Drama
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Local in-memory search data source for Module 3.
 *
 * This class deliberately keeps the same shape as a future remote/data-store backed
 * repository: suspend functions for request-like data and Flow for observable history.
 */
class MockSearchRepository(
    private val dramas: List<Drama> = MockDramas.theaterList,
) : SearchRepository {
    // MutableStateFlow is enough for 3.2 because history only lives while the app process
    // is alive. Module 3.7 will replace this storage detail with DataStore persistence.
    private val searchHistory = MutableStateFlow<List<String>>(emptyList())

    override suspend fun searchDramas(keyword: String): List<Drama> {
        val normalizedKeyword = keyword.trim()
        if (normalizedKeyword.isBlank()) return emptyList()

        // Match title and tags here so search result, hot search, and theater card data
        // can reuse the same Drama model without a separate SearchDrama model.
        return dramas.filter { drama ->
            drama.title.contains(normalizedKeyword, ignoreCase = true) ||
                drama.tags.any { tag -> tag.contains(normalizedKeyword, ignoreCase = true) }
        }
    }

    override suspend fun getHotSearches(): List<Drama> {
        return dramas.take(HOT_SEARCH_LIMIT)
    }

    override suspend fun getGuessLikes(): List<Drama> {
        return dramas.take(GUESS_LIKE_LIMIT)
    }

    override suspend fun getSuggestions(keyword: String): List<String> {
        val normalizedKeyword = keyword.trim()
        if (normalizedKeyword.isBlank()) return emptyList()

        // Suggestions are de-duplicated because the same tag can appear on many dramas.
        // Keeping only strings makes the later suggesting UI simple to render.
        return dramas
            .flatMap { drama -> listOf(drama.title) + drama.tags }
            .filter { item -> item.contains(normalizedKeyword, ignoreCase = true) }
            .distinct()
            .take(SUGGESTION_LIMIT)
    }

    override fun getSearchHistory(): Flow<List<String>> {
        return searchHistory.asStateFlow()
    }

    override suspend fun addSearchHistory(keyword: String) {
        val normalizedKeyword = keyword.trim()
        if (normalizedKeyword.isBlank()) return

        // Put the latest keyword first. Re-adding an old keyword moves it to the top,
        // matching common search-history behavior in mobile apps.
        searchHistory.value = (listOf(normalizedKeyword) + searchHistory.value)
            .distinct()
            .take(HISTORY_LIMIT)
    }

    override suspend fun removeSearchHistory(keyword: String) {
        val normalizedKeyword = keyword.trim()
        searchHistory.value = searchHistory.value.filterNot { it == normalizedKeyword }
    }

    override suspend fun clearSearchHistory() {
        searchHistory.value = emptyList()
    }

    private companion object {
        const val HOT_SEARCH_LIMIT = 10
        const val GUESS_LIKE_LIMIT = 8
        const val SUGGESTION_LIMIT = 10
        const val HISTORY_LIMIT = 10
    }
}
