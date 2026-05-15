package com.example.hongguolite.ui.screens.search

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.NewReleases
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.hongguolite.data.mock.MockDramas
import com.example.hongguolite.data.model.Drama
import com.example.hongguolite.ui.theme.HongguoLiteTheme
import com.example.hongguolite.ui.theme.HongguoRed

/**
 * 搜索模块的页面入口。
 *
 * 这个 Composable 只负责连接 ViewModel 和纯 UI 内容：
 * - 从 ViewModel 收集 uiState
 * - 把用户事件转发给 ViewModel
 * - 真正的布局交给 SearchScreenContent，方便 Preview 和后续 UI 测试
 */
@Composable
fun SearchScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SearchViewModel = viewModel(factory = SearchViewModelFactory()),
) {
    val uiState by viewModel.uiState.collectAsState()

    // UI 事件不直接改状态，而是交给 ViewModel。这样状态变化集中在 ViewModel，
    // 后续 3.5/3.6 扩展中态和结果页时不会把逻辑散落到 Composable 里。
    SearchScreenContent(
        uiState = uiState,
        onBackClick = onBackClick,
        onKeywordChange = viewModel::onKeywordChange,
        onClearKeyword = viewModel::onClearKeyword,
        onSearch = viewModel::onSearch,
        onDeleteHistory = viewModel::onDeleteHistory,
        onClearAllHistory = viewModel::onClearAllHistory,
        modifier = modifier,
    )
}

@Composable
private fun SearchScreenContent(
    uiState: SearchUiState,
    onBackClick: () -> Unit,
    onKeywordChange: (String) -> Unit,
    onClearKeyword: () -> Unit,
    onSearch: (String) -> Unit,
    onDeleteHistory: (String) -> Unit,
    onClearAllHistory: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF7F7F7))
    ) {
        SearchTopBar(
            keyword = uiState.keyword,
            onBackClick = onBackClick,
            onKeywordChange = onKeywordChange,
            onClearKeyword = onClearKeyword,
            onSearch = { onSearch(uiState.keyword) },
        )

        // 搜索页主体只根据 SearchUiState 切换。这里先完整实现 PreSearch，
        // Suggesting 和 ShowingResult 保留占位，后续子任务替换对应分支即可。
        when (uiState) {
            is SearchUiState.PreSearch -> SearchPreContent(
                state = uiState,
                onSearch = onSearch,
                onDeleteHistory = onDeleteHistory,
                onClearAllHistory = onClearAllHistory,
                modifier = Modifier.weight(1f),
            )

            is SearchUiState.Suggesting -> SearchTemporaryState(
                title = "联想列表将在 3.5 实现",
                detail = "当前关键词：${uiState.keyword}",
                modifier = Modifier.weight(1f),
            )

            is SearchUiState.ShowingResult -> SearchTemporaryState(
                title = if (uiState.isLoading) "正在搜索..." else "搜索结果页将在 3.6 完善",
                detail = "当前结果数：${uiState.results.size}",
                modifier = Modifier.weight(1f),
            )
        }
    }
}

@Composable
private fun SearchTopBar(
    keyword: String,
    onBackClick: () -> Unit,
    onKeywordChange: (String) -> Unit,
    onClearKeyword: () -> Unit,
    onSearch: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(start = 4.dp, top = 8.dp, end = 12.dp, bottom = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconButton(onClick = onBackClick) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "返回",
                tint = Color(0xFF222222),
            )
        }

        // TextField 的 value 来自 uiState.keyword；输入事件回到 ViewModel。
        // 这样输入框展示和防抖计算使用同一份状态来源。
        TextField(
            value = keyword,
            onValueChange = onKeywordChange,
            modifier = Modifier
                .weight(1f)
                .height(50.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(Color(0xFFF3F3F3)),
            placeholder = {
                Text(text = "我的26岁女房客", color = Color(0xFF9A9A9A), fontSize = 14.sp)
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null,
                    tint = Color(0xFF999999),
                )
            },
            trailingIcon = {
                if (keyword.isNotEmpty()) {
                    IconButton(onClick = onClearKeyword) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "清空",
                            tint = Color(0xFF999999),
                        )
                    }
                }
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = { onSearch() }),
        )

        Text(
            text = "搜索",
            color = HongguoRed,
            fontSize = 15.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier
                .padding(start = 12.dp)
                .clickable(onClick = onSearch),
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun SearchPreContent(
    state: SearchUiState.PreSearch,
    onSearch: (String) -> Unit,
    onDeleteHistory: (String) -> Unit,
    onClearAllHistory: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current

    // 前置页内容较多，用 LazyColumn 避免小屏设备内容被挤出屏幕后无法滚动。
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp),
    ) {
        item {
            QuickEntryRow(
                onEntryClick = { label ->
                    Toast.makeText(context, "$label 功能后续实现", Toast.LENGTH_SHORT).show()
                },
            )
        }

        if (state.history.isNotEmpty()) {
            item {
                // 历史记录在 3.2 先来自内存 Flow，3.7 会替换为 DataStore 持久化。
                SectionHeader(
                    title = "搜索历史",
                    actionText = "清空",
                    onActionClick = onClearAllHistory,
                )
                Spacer(modifier = Modifier.height(10.dp))
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    state.history.forEach { keyword ->
                        HistoryChip(
                            keyword = keyword,
                            onClick = { onSearch(keyword) },
                            onDeleteClick = { onDeleteHistory(keyword) },
                        )
                    }
                }
            }
        }

        item {
            // 猜你想搜是轻量入口：点击后直接发起搜索，不进入中态。
            SectionHeader(title = "猜你想搜")
            Spacer(modifier = Modifier.height(10.dp))
            GuessLikeGrid(
                dramas = state.guessLikes.take(6),
                onDramaClick = { drama -> onSearch(drama.title) },
            )
        }

        item {
            // 热搜榜保留排名和火焰热度，这是搜索前置页的关键识别元素。
            SectionHeader(title = "短剧热搜榜")
            Spacer(modifier = Modifier.height(10.dp))
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                state.hotSearches.take(10).forEachIndexed { index, drama ->
                    HotSearchRow(
                        rank = index + 1,
                        drama = drama,
                        onClick = { onSearch(drama.title) },
                    )
                }
            }
        }
    }
}

@Composable
private fun QuickEntryRow(
    onEntryClick: (String) -> Unit,
) {
    val entries = listOf(
        QuickEntry("识剧", Icons.Default.Movie, Color(0xFFFF8A24)),
        QuickEntry("排行", Icons.Default.LocalFireDepartment, Color(0xFFE6303C)),
        QuickEntry("上新", Icons.Default.NewReleases, Color(0xFF32B7A6)),
        QuickEntry("演员", Icons.Default.Groups, Color(0xFF7D63F3)),
    )

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        entries.forEach { entry ->
            Column(
                modifier = Modifier.clickable { onEntryClick(entry.label) },
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Box(
                    modifier = Modifier
                        .size(54.dp)
                        .clip(CircleShape)
                        .background(entry.color.copy(alpha = 0.13f)),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        imageVector = entry.icon,
                        contentDescription = entry.label,
                        tint = entry.color,
                        modifier = Modifier.size(28.dp),
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(text = entry.label, fontSize = 13.sp, color = Color(0xFF333333))
            }
        }
    }
}

@Composable
private fun SectionHeader(
    title: String,
    actionText: String? = null,
    onActionClick: (() -> Unit)? = null,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = title,
            color = Color(0xFF222222),
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
        )
        if (actionText != null && onActionClick != null) {
            Text(
                text = actionText,
                color = Color(0xFF888888),
                fontSize = 13.sp,
                modifier = Modifier.clickable(onClick = onActionClick),
            )
        }
    }
}

@Composable
private fun HistoryChip(
    keyword: String,
    onClick: () -> Unit,
    onDeleteClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(18.dp))
            .background(Color.White)
            .clickable(onClick = onClick)
            .padding(start = 10.dp, top = 7.dp, end = 6.dp, bottom = 7.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = Icons.Default.History,
            contentDescription = null,
            tint = Color(0xFF999999),
            modifier = Modifier.size(16.dp),
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(text = keyword, color = Color(0xFF555555), fontSize = 13.sp)
        Spacer(modifier = Modifier.width(2.dp))
        Icon(
            imageVector = Icons.Default.Close,
            contentDescription = "删除历史",
            tint = Color(0xFFBBBBBB),
            modifier = Modifier
                .size(16.dp)
                .clickable(onClick = onDeleteClick),
        )
    }
}

@Composable
private fun GuessLikeGrid(
    dramas: List<Drama>,
    onDramaClick: (Drama) -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        dramas.chunked(3).forEach { rowDramas ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                rowDramas.forEach { drama ->
                    GuessLikeCard(
                        drama = drama,
                        onClick = { onDramaClick(drama) },
                        modifier = Modifier.weight(1f),
                    )
                }
                repeat(3 - rowDramas.size) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
private fun GuessLikeCard(
    drama: Drama,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .height(82.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(Color.White)
            .clickable(onClick = onClick)
            .padding(10.dp),
        verticalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = drama.title,
            color = Color(0xFF222222),
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
        )
        Text(
            text = drama.tags.firstOrNull().orEmpty(),
            color = HongguoRed,
            fontSize = 11.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Composable
private fun HotSearchRow(
    rank: Int,
    drama: Drama,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(Color.White)
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 11.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        RankBadge(rank = rank)
        Spacer(modifier = Modifier.width(10.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = drama.title,
                color = Color(0xFF222222),
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = drama.tags.take(2).joinToString(" · "),
                color = Color(0xFF999999),
                fontSize = 12.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.LocalFireDepartment,
                contentDescription = null,
                tint = HongguoRed,
                modifier = Modifier.size(16.dp),
            )
            Text(
                text = drama.heatCount,
                color = HongguoRed,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}

@Composable
private fun RankBadge(rank: Int) {
    val color = when (rank) {
        1 -> Color(0xFFE6303C)
        2 -> Color(0xFFFF8A24)
        3 -> Color(0xFFFFBD4A)
        else -> Color(0xFFB8B8B8)
    }

    Box(
        modifier = Modifier
            .size(24.dp)
            .clip(RoundedCornerShape(7.dp))
            .background(color),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = rank.toString(),
            color = Color.White,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
        )
    }
}

@Composable
private fun SearchTemporaryState(
    title: String,
    detail: String,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = title, color = Color(0xFF666666), fontSize = 16.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = detail, color = Color(0xFF999999), fontSize = 13.sp)
        }
    }
}

private data class QuickEntry(
    val label: String,
    val icon: ImageVector,
    val color: Color,
)



@Preview(showBackground = true, widthDp = 393, heightDp = 852)
@Composable
private fun SearchPreContentPreview() {
    HongguoLiteTheme(dynamicColor = false) {
        SearchScreenContent(
            uiState = SearchUiState.PreSearch(
                guessLikes = MockDramas.theaterList.take(6),
                hotSearches = MockDramas.theaterList.take(10),
                history = listOf("逆袭", "女房客", "总裁"),
            ),
            onBackClick = {},
            onKeywordChange = {},
            onClearKeyword = {},
            onSearch = {},
            onDeleteHistory = {},
            onClearAllHistory = {},
        )
    }
}
