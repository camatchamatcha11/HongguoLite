package com.example.hongguolite.navigation

import org.junit.Assert.assertEquals
import org.junit.Test

class RoutesTest {
    @Test
    fun routes_includeSearchEntry() {
        assertEquals("search", Routes.SEARCH)
    }
}
