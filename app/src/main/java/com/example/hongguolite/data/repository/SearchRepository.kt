package com.example.hongguolite.data.repository

import com.example.hongguolite.data.model.Drama
import kotlinx.coroutines.flow.Flow

interface SearchRepository {
    suspend fun searchDramas(keyword: String): List<Drama>
    suspend fun getHotSearches(): List<Drama>
    suspend fun getGuessLikes(): List<Drama>
    suspend fun getSuggestions(keyword: String): List<String>
    fun getSearchHistory(): Flow<List<String>>
    suspend fun addSearchHistory(keyword: String)
    suspend fun removeSearchHistory(keyword: String)
    suspend fun clearSearchHistory()
}
