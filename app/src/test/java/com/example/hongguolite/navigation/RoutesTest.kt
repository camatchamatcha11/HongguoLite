package com.example.hongguolite.navigation

import org.junit.Assert.assertEquals
import org.junit.Test

class RoutesTest {
    @Test
    fun routes_includeSearchEntry() {
        // Search is intentionally centralized in Routes so UI files do not hard-code
        // the same "search" string in multiple click handlers.
        assertEquals("search", Routes.SEARCH)
    }
}
