package com.example.hongguolite.data.model

data class TheaterTab(
    val id: String,
    val label: String
)

val theaterTabs = listOf(
    TheaterTab(id = "find_drama", label = "找剧"),
    TheaterTab(id = "community", label = "看剧"),
    TheaterTab(id = "manga", label = "漫画"),
    TheaterTab(id = "movie", label = "电影"),
    TheaterTab(id = "tv", label = "电视剧"),
    TheaterTab(id = "audiobook", label = "听书"),
    TheaterTab(id = "novel", label = "小说"),
)
