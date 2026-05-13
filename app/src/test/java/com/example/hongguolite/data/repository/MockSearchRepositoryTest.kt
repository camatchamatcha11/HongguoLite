package com.example.hongguolite.data.repository

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class MockSearchRepositoryTest {
    private val repository = MockSearchRepository()

    @Test
    fun searchDramas_blankKeywordReturnsEmptyList() = runBlocking {
        assertTrue(repository.searchDramas("   ").isEmpty())
    }

    @Test
    fun searchDramas_matchesTitleOrTags() = runBlocking {
        val firstDrama = repository.getGuessLikes().first()
        val tagKeyword = firstDrama.tags.first()

        val results = repository.searchDramas(tagKeyword)

        assertTrue(results.any { it.id == firstDrama.id })
    }

    @Test
    fun getSuggestions_blankKeywordReturnsEmptyList() = runBlocking {
        assertTrue(repository.getSuggestions("").isEmpty())
    }

    @Test
    fun getSuggestions_returnsTitleAndTagMatchesWithoutDuplicates() = runBlocking {
        val firstDrama = repository.getGuessLikes().first()
        val keyword = firstDrama.tags.first()

        val suggestions = repository.getSuggestions(keyword)

        assertTrue(suggestions.isNotEmpty())
        assertEquals(suggestions.distinct(), suggestions)
    }

    @Test
    fun searchHistory_keepsLatestFirstAndDeduplicates() = runBlocking {
        repository.addSearchHistory(" first ")
        repository.addSearchHistory("second")
        repository.addSearchHistory("first")

        assertEquals(listOf("first", "second"), repository.getSearchHistory().first())
    }

    @Test
    fun searchHistory_removesSingleKeywordAndCanClearAll() = runBlocking {
        repository.addSearchHistory("first")
        repository.addSearchHistory("second")

        repository.removeSearchHistory("first")

        assertEquals(listOf("second"), repository.getSearchHistory().first())

        repository.clearSearchHistory()

        assertTrue(repository.getSearchHistory().first().isEmpty())
    }
}
