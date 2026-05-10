package com.example.hongguolite.data.model

import org.junit.Assert.assertEquals
import org.junit.Test

class TheaterTabTest {

    @Test
    fun theaterTabs_keepExpectedOrder() {
        assertEquals(
            listOf("找剧", "看剧", "漫画", "电影", "电视剧", "听书", "小说"),
            theaterTabs.map { it.label }
        )
    }
}
