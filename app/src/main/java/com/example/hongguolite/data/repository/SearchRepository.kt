package com.example.hongguolite.data.repository

import com.example.hongguolite.data.model.Drama
import kotlinx.coroutines.flow.Flow

/**
 * Search module data boundary.
 *
 * UI and ViewModel code should depend on this interface instead of reading
 * MockDramas directly. Module 3 uses MockSearchRepository, and later network
 * work can replace the implementation without changing search screen logic.
 */
interface SearchRepository {
    /**
     * Returns dramas whose title or tags contain [keyword].
     *
     * Blank keywords are treated as no search request and should return an empty list.
     */
    suspend fun searchDramas(keyword: String): List<Drama>

    /** Returns the dramas shown in the pre-search hot ranking area. */
    suspend fun getHotSearches(): List<Drama>

    /** Returns the dramas shown in the pre-search "guess you want to search" area. */
    suspend fun getGuessLikes(): List<Drama>

    /**
     * Returns lightweight suggestion text for the typing state.
     *
     * Suggestions are strings rather than Drama objects because the middle state
     * can mix title suggestions and tag/category suggestions.
     */
    suspend fun getSuggestions(keyword: String): List<String>

    /**
     * Search history is exposed as Flow because the UI should update automatically
     * after add/remove/clear, and Module 3.7 will move this stream to DataStore.
     */
    fun getSearchHistory(): Flow<List<String>>

    /** Adds a keyword to history, with implementation-specific deduping and max size rules. */
    suspend fun addSearchHistory(keyword: String)

    /** Removes one keyword from history. */
    suspend fun removeSearchHistory(keyword: String)

    /** Clears all search history records. */
    suspend fun clearSearchHistory()
}
